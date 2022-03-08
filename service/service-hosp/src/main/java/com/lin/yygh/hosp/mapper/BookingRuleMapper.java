package com.lin.yygh.hosp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.hosp.mapper.provider.BookingRuleMapperProvider;
import com.lin.yygh.model.hosp.v2.BookingRule2;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface BookingRuleMapper extends BaseMapper<BookingRule2> {
    @SelectProvider(type = BookingRuleMapperProvider.class, method = "listBookingRuleByHospitalId")
    List<BookingRule2> listBookingRuleByHospitalId(@Param("hospitalId")Long HospitalId);
}
