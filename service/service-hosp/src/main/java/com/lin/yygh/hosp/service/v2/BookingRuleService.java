package com.lin.yygh.hosp.service.v2;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.yygh.model.hosp.v2.BookingRule2;

import java.util.List;

public interface BookingRuleService extends IService<BookingRule2> {
    public List<BookingRule2> listByHospitalId(Long hospitalId);
}
