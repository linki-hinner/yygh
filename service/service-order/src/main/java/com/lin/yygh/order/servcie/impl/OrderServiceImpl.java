package com.lin.yygh.order.servcie.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.yygh.common.exception.YyghException;
import com.lin.yygh.common.helper.HttpRequestHelper;
import com.lin.yygh.common.result.ResultCodeEnum;
import com.lin.yygh.enums.OrderStatusEnum;
import com.lin.yygh.hosp.HospitalFeignClient;
import com.lin.yygh.model.order.OrderInfo;
import com.lin.yygh.model.user.Patient;
import com.lin.yygh.order.mapper.OrderMapper;
import com.lin.yygh.order.servcie.OrderService;
import com.lin.yygh.rabbit.constant.MqConst;
import com.lin.yygh.rabbit.servcie.RabbitService;
import com.lin.yygh.user.client.PatientFeignClient;
import com.lin.yygh.vo.hosp.ScheduleOrderVo;
import com.lin.yygh.vo.msm.MsmVo;
import com.lin.yygh.vo.order.*;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {
    private final PatientFeignClient patientFeignClient;
    private final HospitalFeignClient hospitalFeignClient;
    private final RabbitService rabbitService;

    @Override
    public Long saveOrder(String scheduleId, Long patientId) {
        Patient patient = patientFeignClient.getPatient(patientId);
        if(null == patient) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        ScheduleOrderVo scheduleOrderVo = hospitalFeignClient.getScheduleOrderVo(scheduleId);
        if(null == scheduleOrderVo) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //当前时间不可以预约
        if(new DateTime(scheduleOrderVo.getStartTime()).isAfterNow()
                || new DateTime(scheduleOrderVo.getEndTime()).isBeforeNow()) {
            throw new YyghException(ResultCodeEnum.TIME_NO);
        }
        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(scheduleOrderVo.getHoscode());
        if(scheduleOrderVo.getAvailableNumber() <= 0) {
            throw new YyghException(ResultCodeEnum.NUMBER_NO);
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(scheduleOrderVo, orderInfo);
        String outTradeNo = System.currentTimeMillis() + ""+ new Random().nextInt(100);
        orderInfo.setOutTradeNo(outTradeNo);
        orderInfo.setScheduleId(scheduleId);
        orderInfo.setUserId(patient.getUserId());
        orderInfo.setPatientId(patientId);
        orderInfo.setPatientName(patient.getName());
        orderInfo.setPatientPhone(patient.getPhone());
        orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());
        this.save(orderInfo);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",orderInfo.getHoscode());
        paramMap.put("depcode",orderInfo.getDepcode());
        paramMap.put("hosScheduleId",orderInfo.getScheduleId());
        paramMap.put("reserveDate",new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd"));
        paramMap.put("reserveTime", orderInfo.getReserveTime());
        paramMap.put("amount",orderInfo.getAmount());
        paramMap.put("name", patient.getName());
        paramMap.put("certificatesType",patient.getCertificatesType());
        paramMap.put("certificatesNo", patient.getCertificatesNo());
        paramMap.put("sex",patient.getSex());
        paramMap.put("birthdate", patient.getBirthdate());
        paramMap.put("phone",patient.getPhone());
        paramMap.put("isMarry", patient.getIsMarry());
        paramMap.put("provinceCode",patient.getProvinceCode());
        paramMap.put("cityCode", patient.getCityCode());
        paramMap.put("districtCode",patient.getDistrictCode());
        paramMap.put("address",patient.getAddress());
        //联系人
        paramMap.put("contactsName",patient.getContactsName());
        paramMap.put("contactsCertificatesType", patient.getContactsCertificatesType());
        paramMap.put("contactsCertificatesNo",patient.getContactsCertificatesNo());
        paramMap.put("contactsPhone",patient.getContactsPhone());
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        String sign = HttpRequestHelper.getSign(paramMap, signInfoVo.getSignKey());
        paramMap.put("sign", sign);
        JSONObject result = HttpRequestHelper.sendRequest(paramMap, signInfoVo.getApiUrl()+"/order/submitOrder");

        if(result.getInteger("code") == 200) {
            JSONObject jsonObject = result.getJSONObject("data");
            //预约记录唯一标识（医院预约记录主键）
            String hosRecordId = jsonObject.getString("hosRecordId");
            //预约序号
            Integer number = jsonObject.getInteger("number");;
            //取号时间
            String fetchTime = jsonObject.getString("fetchTime");;
            //取号地址
            String fetchAddress = jsonObject.getString("fetchAddress");;
            //更新订单
            orderInfo.setHosRecordId(hosRecordId);
            orderInfo.setNumber(number);
            orderInfo.setFetchTime(fetchTime);
            orderInfo.setFetchAddress(fetchAddress);
            baseMapper.updateById(orderInfo);
            //排班可预约数
            Integer reservedNumber = jsonObject.getInteger("reservedNumber");
            //排班剩余预约数
            Integer availableNumber = jsonObject.getInteger("availableNumber");
            //发送mq信息更新号源和短信通知
            OrderMqVo orderMqVo = new OrderMqVo();
            orderMqVo.setScheduleId(scheduleId);
            orderMqVo.setReservedNumber(reservedNumber);
            orderMqVo.setAvailableNumber(availableNumber);

            MsmVo msmVo = new MsmVo();
            msmVo.setPhone(orderInfo.getPatientPhone());
            msmVo.setTemplateCode("SMS_194640721");
            String reserveDate =
                    new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd")
                            + (orderInfo.getReserveTime()==0 ? "上午": "下午");
            Map<String,Object> param = new HashMap<String,Object>(){{
                put("title", orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle());
                put("amount", orderInfo.getAmount());
                put("reserveDate", reserveDate);
                put("name", orderInfo.getPatientName());
                put("quitTime", new DateTime(orderInfo.getQuitTime()).toString("yyyy-MM-dd HH:mm"));
            }};
            msmVo.setParam(param);

            orderMqVo.setMsmVo(msmVo);
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT, MqConst.ROUTING_ORDER, orderMqVo);
        } else {
            throw new YyghException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        }
        return orderInfo.getId();
    }

    @Override
    public IPage<OrderInfo> selectPage(OrderQueryVo orderQueryVo) {
//        //orderQueryVo获取条件值
//        String name = orderQueryVo.getKeyword(); //医院名称
//        Long patientId = orderQueryVo.getPatientId(); //就诊人名称
//        String orderStatus = orderQueryVo.getOrderStatus(); //订单状态
//        String reserveDate = orderQueryVo.getReserveDate();//安排时间
//        String createTimeBegin = orderQueryVo.getCreateTimeBegin();
//        String createTimeEnd = orderQueryVo.getCreateTimeEnd();
//        //对条件值进行非空判断
//        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
//        if (!StringUtils.isEmpty(name)) {
//            wrapper.like("hosname", name);
//        }
//        if (null == patientId) {
//            wrapper.eq("patient_id", patientId);
//        }
//        if (!StringUtils.isEmpty(orderStatus)) {
//            wrapper.eq("order_status", orderStatus);
//        }
//        if (!StringUtils.isEmpty(reserveDate)) {
//            wrapper.ge("reserve_date", reserveDate);
//        }
//        if (!StringUtils.isEmpty(createTimeBegin)) {
//            wrapper.ge("create_time", createTimeBegin);
//        }
//        if (!StringUtils.isEmpty(createTimeEnd)) {
//            wrapper.le("create_time", createTimeEnd);
//        }
        //调用mapper的方法
//        IPage<OrderInfo> pages = baseMapper.selectPage(pageParam, wrapper);
//        //编号变成对应值封装
//        pages.getRecords().forEach(this::fillOrderInfo);

        List<OrderInfo> orderQueryVoList = baseMapper.getPageByOrderQueryVo(orderQueryVo);
        orderQueryVoList.forEach(this::fillOrderInfo);

        int total = baseMapper.getCountByOrderQueryVo(orderQueryVo);

        Page<OrderInfo> orderInfoPage = new Page<>();
        orderInfoPage.setRecords(orderQueryVoList);
        orderInfoPage.setTotal(total);

        return orderInfoPage;
    }

    @Override
    public OrderInfo getOrder(String orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        return this.fillOrderInfo(orderInfo);
    }

    @Override
    public Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo) {
        List<OrderCountVo> count = baseMapper.ListOrderCountVo(orderCountQueryVo);

        List<String> dateList = count.stream()
                .map(OrderCountVo::getReserveDate)
                .collect(Collectors.toList());

        List<Integer> countList = count.stream()
                .map(OrderCountVo::getCount)
                .collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        map.put("dateList", dateList);
        map.put("countList", countList);
        return map;
    }

    private OrderInfo fillOrderInfo(OrderInfo orderInfo) {
        orderInfo.getParam().put("orderStatusString", OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus()));
        return orderInfo;
    }
}

