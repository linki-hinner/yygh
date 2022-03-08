package com.lin.yygh.hosp.mapper.provider;

import com.lin.yygh.model.base.BaseEntity;

import java.util.Map;

public class BookingRuleMapperProvider {
    public String listBookingRuleByHospitalId(Map<String, Object> params){
        return "select * from booking_rule where is_deleted = " + BaseEntity.STATE_SURVIVE +
                " and hospital_id = #{hospitalId}";
    }
}
