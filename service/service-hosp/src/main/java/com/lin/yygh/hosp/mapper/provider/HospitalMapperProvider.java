package com.lin.yygh.hosp.mapper.provider;

import com.lin.yygh.model.base.BaseEntity;
import com.lin.yygh.model.hosp.v2.Hospital2;
import com.lin.yygh.vo.hosp.DepartmentQueryVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class HospitalMapperProvider {

    public String getByHoscode(Map<String, Object> params){
        return "select hospital_id, hoscode, depname, intro, big_id from department " +
                " where is_deleted = " + BaseEntity.STATE_SURVIVE +
                " and hoscode = #{hoscode} ";
    }

    public String listByHoscode(Map<String, Object> params){
        String hoscode = ((String) params.get("hoscode"));

        StringBuilder sql = new StringBuilder();
        sql.append("select hospital_id, depname, intro, big_id from department ");
        sql.append(" where is_deleted = ").append(BaseEntity.STATE_SURVIVE);
        if (StringUtils.isNotBlank(hoscode)) {
            sql.append(" and hoscode = #{hoscode} ");
        }
        return sql.toString();
    }


    public String insertOrUpdateByHoscode(Map<String, Object> params){
        Hospital2 hospital2 = (Hospital2) params.get("hospital");

        StringBuilder sql = new StringBuilder();
        sql.append(" insert into department (hospital_id, hoscode, depcode, depname, intro) ");
        sql.append(" value (#{hospital.hospitalId}, #{hospital.hoscode}, {hospital.address}, {hospital.route}, #{hospital.logoData}, #{hospital.intro}, #{hosptial.status})");
        sql.append(" on duplicate key update");
        if(StringUtils.isNotBlank(hospital2.getAddress())){
            sql.append(" set address = #{hospital.address}");
        }
        if(StringUtils.isNotBlank(hospital2.getRoute())){
            sql.append(" set route = #{hospital.route}");
        }
        if(StringUtils.isNotBlank(hospital2.getLogoData())){
            sql.append(" set logoData = #{hospital.logoData}");
        }
        if(StringUtils.isNotBlank(hospital2.getIntro())){
            sql.append(" set intro = #{hospital.intro}");
        }
        return sql.toString();
    }


    public String listByDepartmentQueryVo(Map<String, Object> params){
        DepartmentQueryVo vo = (DepartmentQueryVo) params.get("vo");

        StringBuilder sql = new StringBuilder();
        sql.append("select hospital_id, depname, intro, big_id from department ").append(where(vo));

        int current = ObjectUtils.firstNonNull(vo.getCurrent(), 1);
        int size = ObjectUtils.firstNonNull(vo.getSize(), 10);
        sql.append(" limit ").append((current - 1) * size);
        sql.append(" , ").append(size);

        return sql.toString();
    }

    public String countByDepartmentQueryVo(Map<String, Object> params){
        DepartmentQueryVo vo = (DepartmentQueryVo) params.get("vo");
        return "select count(*)" + where(vo);
    }

    private String where(DepartmentQueryVo vo){
        StringBuilder sql = new StringBuilder();
        sql.append(" where is_deleted = ").append(BaseEntity.STATE_SURVIVE);
        if (StringUtils.isNotBlank(vo.getHoscode())) {
            sql.append(" and hoscode = #{hoscode} ");
        }
        if (StringUtils.isNotBlank(vo.getDepcode())) {
            sql.append(" and depcode = #{depcode} ");
        }
        if (StringUtils.isNotBlank(vo.getDepname())) {
            sql.append(" and depname = #{depname} ");
        }
//        if (StringUtils.isNotBlank(vo.getBigcode())) {
//            sql.append(" and depcode = #{depcode} ");
//        }
        return sql.toString();
    }
}
