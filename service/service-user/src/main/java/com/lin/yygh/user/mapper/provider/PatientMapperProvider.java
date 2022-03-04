package com.lin.yygh.user.mapper.provider;

import com.lin.yygh.model.base.BaseEntity;

import java.util.Map;

public class PatientMapperProvider {
    private final static String BASE_SELECT = "select id, user_id, name, certificates_type, certificates_no, " +
            " sex, birthdate, phone, is_marry, province_code, city_code, district_code, address, contacts_name, " +
            " contacts_certificates_type, contacts_certificates_no, contacts_phone, card_no, is_insure, status " +
            " from patient " +
            " where is_deleted = " + BaseEntity.STATE_SURVIVE;

    public String ListByUserId(Map<String, Object> params) {
        Long userId = (Long) params.get("userId");
        return BASE_SELECT + " and user_id = " + userId;
    }
}
