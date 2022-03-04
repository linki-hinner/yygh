package com.lin.yygh.hosp.mapper.provider;

import com.lin.yygh.model.base.BaseEntity;

import java.util.Map;

public class HospitalSetMapperProvider {
    public final static String SELECT_BASE = " select id, hosname, hoscode, api_url, sign_key, contacts_name, contacts_phone, status "
            + " from hospital_set "
            + " where is_deleted = " + BaseEntity.STATE_SURVIVE;
    public String getOneByHoscode(Map<String, Object> params){
        return SELECT_BASE + " and hoscode = #{hoscode}";
    }
}
