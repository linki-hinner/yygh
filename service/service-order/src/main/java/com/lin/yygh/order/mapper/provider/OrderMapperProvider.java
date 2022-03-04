package com.lin.yygh.order.mapper.provider;

import com.lin.yygh.model.base.BaseEntity;
import com.lin.yygh.vo.order.OrderCountQueryVo;
import com.lin.yygh.vo.order.OrderQueryVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

public class OrderMapperProvider {
    public final static String BASE_SELECT = "select id, user_id, out_trade_no, hoscode, hosname, depname, title, hos_schedule_id, " +
            " reserve_date, reserve_time, patient_id, patient_name, patient_phone, hos_record_id, number, fetch_time, " +
            " fetch_address, amount, quit_time, order_status, schedule_id " +
            " from order_info " +
            " where is_deleted = " + BaseEntity.STATE_SURVIVE;

    public final static String BASE_COUNT = "select count(*) " +
            " from order_info " +
            " where is_deleted = " + BaseEntity.STATE_SURVIVE;


    public final static String BASE_STATISTICS = "select count(*) `count`, reserve_date reserveDate  " +
            " from order_info " +
            " where is_deleted = " + BaseEntity.STATE_SURVIVE;

    public String getPageByOrderQueryVo(Map<String, Object> params){
        StringBuilder sql = new StringBuilder(BASE_SELECT);
        OrderQueryVo orderQueryVo = (OrderQueryVo) params.get("queryVo");
        fillWhere(orderQueryVo, sql);
        fillLimit(orderQueryVo, sql);
        return sql.toString();
    }


    public String getCountByOrderQueryVo(Map<String, Object> params){
        StringBuilder sql = new StringBuilder(BASE_COUNT);
        OrderQueryVo orderQueryVo = (OrderQueryVo) params.get("queryVo");
        fillWhere(orderQueryVo, sql);
        return sql.toString();
    }

    public String ListOrderCountVo(Map<String, Object> params){
        OrderCountQueryVo vo = (OrderCountQueryVo) params.get("vo");

        StringBuilder sql = new StringBuilder(BASE_STATISTICS);
        if(StringUtils.isNotBlank(vo.getHosname())) {
            sql.append(" and hosname like CONCAT('%', #{vo.hosname}, '%')");
        }
        if (StringUtils.isNotBlank(vo.getReserveDateBegin()) && StringUtils.isNotBlank(vo.getReserveDateEnd())) {
            sql.append(" and reserve_date >= #{vo.reserveDateBegin} ");
            sql.append(" and reserve_date <= #{vo.getReserveDateEnd} ");
        }
        sql.append(" group by reserve_date ");
        sql.append(" order by reserve_date ");
        return sql.toString();
    }

    private void fillWhere(OrderQueryVo orderQueryVo, StringBuilder sql) {
        String name = orderQueryVo.getKeyword(); //医院名称
        Long patientId = orderQueryVo.getPatientId(); //就诊人名称
        String orderStatus = orderQueryVo.getOrderStatus(); //订单状态
        String reserveDate = orderQueryVo.getReserveDate();//安排时间
        String createTimeBegin = orderQueryVo.getCreateTimeBegin();
        String createTimeEnd = orderQueryVo.getCreateTimeEnd();


        if (!StringUtils.isEmpty(name)) {
            sql.append(" and hosname like %").append(name).append("%");
        }
        if (Objects.nonNull(patientId)) {
            sql.append(" and patient_id = ").append(patientId);
        }
        if (!StringUtils.isEmpty(orderStatus)) {
            sql.append(" and order_status = #{queryVo.orderStatus}");
        }
        if (!StringUtils.isEmpty(reserveDate)) {
            sql.append(" and reserve_date >= #{queryVo.reserveDate}");
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            sql.append(" and create_time >= #{queryVo.createTimeBegin}");
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            sql.append(" and create_time <= #{queryVo.createTimeEnd}");
        }
    }

    private void fillLimit(OrderQueryVo orderQueryVo, StringBuilder sql) {
        int current = ObjectUtils.firstNonNull(orderQueryVo.getCurrent(), 1);
        int size = ObjectUtils.firstNonNull(orderQueryVo.getSize(), 10);
        int start = (current - 1) * size;
        sql.append(" limit ").append(start).append(", ").append(size);
    }
}
