package com.lin.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.yygh.common.exception.YyghException;
import com.lin.yygh.common.result.ResultCodeEnum;
import com.lin.yygh.hosp.mapper.HospitalSetMapper;
import com.lin.yygh.hosp.service.HospitalSetService;
import com.lin.yygh.model.hosp.HospitalSet;
import com.lin.yygh.vo.order.SignInfoVo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Override
    public Optional<HospitalSet> getHospitalSetByHoscode(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        return Optional.ofNullable(hospitalSet);
    }

    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }
}
