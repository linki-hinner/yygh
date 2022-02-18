package com.lin.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.yygh.model.hosp.HospitalSet;
import com.lin.yygh.vo.order.SignInfoVo;

import java.util.Optional;

public interface HospitalSetService extends IService<HospitalSet> {
    Optional<HospitalSet> getHospitalSetByHoscode(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);
}
