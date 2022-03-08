package com.lin.yygh.hosp.service.impl.v2;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.yygh.hosp.mapper.BookingRuleMapper;
import com.lin.yygh.hosp.service.v2.BookingRuleService;
import com.lin.yygh.model.hosp.v2.BookingRule2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingRuleServiceImpl extends ServiceImpl<BookingRuleMapper, BookingRule2>implements BookingRuleService {

    @Override
    public List<BookingRule2> listByHospitalId(Long hospitalId) {
        return baseMapper.listBookingRuleByHospitalId(hospitalId);
    }
}
