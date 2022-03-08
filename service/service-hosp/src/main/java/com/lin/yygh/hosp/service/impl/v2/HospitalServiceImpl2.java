package com.lin.yygh.hosp.service.impl.v2;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.lin.yygh.cmn.client.DictFeignClient;
import com.lin.yygh.hosp.mapper.HospitalMapper;
import com.lin.yygh.hosp.service.v2.BookingRuleService;
import com.lin.yygh.hosp.service.v2.HospitalService2;
import com.lin.yygh.model.hosp.Hospital;
import com.lin.yygh.model.hosp.v2.BookingRule2;
import com.lin.yygh.model.hosp.v2.Hospital2;
import com.lin.yygh.vo.hosp.HospitalQueryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl2 extends ServiceImpl<HospitalMapper, Hospital2> implements HospitalService2  {
    private final DictFeignClient dictFeignClient;
    private final BookingRuleService bookingRuleService;

    @Override
    public Map<String, Object> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        List<Hospital2> data = baseMapper.listByHospitalQueryVo(hospitalQueryVo);
        long total = baseMapper.countByHospitalQueryVo(hospitalQueryVo);
        Map<String, Object> map = Maps.newHashMap();
        map.put("data", data);
        map.put("total", total);
        return map;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return baseMapper.updateState(id, status);
    }

    @Override
    public Hospital2 show(Long id) {
        Hospital2 hospital = baseMapper.selectById(id);
        setHospitalInfo(hospital);
        return hospital;
    }

    @Override
    public String getHospName(Long id) {
        Hospital2 hospitalByHoscode = baseMapper.selectById(id);
        return hospitalByHoscode != null ? hospitalByHoscode.getHosname() : null;
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        return baseMapper.listByHosnameLike(hosname);
    }

    @Override
    public Map<String, Object> item(Long id) {
//        Map<String, Object> result = new HashMap<>();
//        //医院详情
//        Hospital hospital = hospitalService.getByHoscode(hoscode);
//        this.setHospitalInfo(hospital);
//        result.put("hospital", hospital);
//        //预约规则
//        result.put("bookingRule", hospital.getBookingRule());
//        //不需要重复返回
//        hospital.setBookingRule(null);
//        return result;

        Map<String, Object> result = new HashMap<>();

        Hospital2 hospital = baseMapper.selectById(id);
        result.put("hospital", hospital);
        if (hospital != null) {
            List<BookingRule2> bookingRuleList = bookingRuleService.listByHospitalId(hospital.getId());
            result.put("bookingRule", bookingRuleList);
        }
        return result;
    }


    private void setHospitalInfo(Hospital2 hospital) {
        String hospitalType = dictFeignClient.getName("Hostype", hospital.getHostype().toString());
        String province = dictFeignClient.getName(hospital.getProvinceCode().toString());
        String city = dictFeignClient.getName(hospital.getCityCode().toString());
        String district = dictFeignClient.getName(hospital.getDistrictCode().toString());

        if (hospital.getParam() == null) {
            hospital.setParam(new HashMap<>());
        }
        hospital.getParam().put("hostypeString", hospitalType);
        hospital.getParam().put("fulladdress", province + city + district);
    }

}
