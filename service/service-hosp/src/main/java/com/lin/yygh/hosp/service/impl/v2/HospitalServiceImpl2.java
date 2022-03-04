package com.lin.yygh.hosp.service.impl.v2;

import com.alibaba.fastjson.JSONObject;
import com.lin.yygh.cmn.client.DictFeignClient;
import com.lin.yygh.hosp.mapper.HospitalMapper;
import com.lin.yygh.hosp.repository.HospitalRepository;
import com.lin.yygh.hosp.service.v2.HospitalService2;
import com.lin.yygh.model.hosp.Hospital;
import com.lin.yygh.model.hosp.v2.Hospital2;
import com.lin.yygh.vo.hosp.HospitalQueryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl2 implements HospitalService2 {
    private final HospitalRepository hospitalRepository;
    private final DictFeignClient dictFeignClient;
    private HospitalMapper hospitalMapper;


    @Override
    public void save(Map<String, Object> paramMap) {
        String string = JSONObject.toJSONString(paramMap);
        Hospital2 hospital = JSONObject.parseObject(string, Hospital2.class);

        hospitalMapper.insertOrUpdateByHoscode(hospital);
    }

    @Override
    public Hospital2 getByHoscode(String hoscode) {
        return hospitalMapper.getByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

        Example<Hospital> example = Example.of(hospital, matcher);

        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        List<Hospital> content = pages.getContent();
        content.forEach(this::setHospitalInfo);

        return pages;
    }

    @Override
    public boolean updateStatus(String id, Integer status) {
        boolean result = true;
        try{
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }catch (NoSuchElementException ignored){
            result = false;
        }
        return result;
    }

    @Override
    public Hospital show(String id) {
        try {
            Hospital hospital = hospitalRepository.findById(id).get();
            setHospitalInfo(hospital);
            return hospital;
        }catch (NoSuchElementException ignore){
            return null;
        }
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospitalByHoscode = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospitalByHoscode != null ? hospitalByHoscode.getHosname() : null;
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    @Override
    public Map<String, ?> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        this.setHospitalInfo(hospital);
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    private void setHospitalInfo(Hospital hospital) {
        String hospitalType = dictFeignClient.getName("Hostype", hospital.getHostype());
        String province = dictFeignClient.getName(hospital.getProvinceCode());
        String city = dictFeignClient.getName(hospital.getCityCode());
        String district = dictFeignClient.getName(hospital.getDistrictCode());

        if (hospital.getParam() == null) {
            hospital.setParam(new HashMap<>());
        }
        hospital.getParam().put("hostypeString", hospitalType);
        hospital.getParam().put("fulladdress", province + city + district);
    }

}
