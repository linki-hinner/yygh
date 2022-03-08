package com.lin.yygh.hosp.service.impl;

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
public class HospitalSetServiceImpl extends hostype<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Override
    public Optional<HospitalSet> getHospitalSetByHoscode(String hoscode) {
        return Optional.ofNullable(baseMapper.getOneByHoscode(hoscode));
    }

    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        HospitalSet hospitalSet = baseMapper.getOneByHoscode(hoscode);
        if(null == hospitalSet) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }
}
