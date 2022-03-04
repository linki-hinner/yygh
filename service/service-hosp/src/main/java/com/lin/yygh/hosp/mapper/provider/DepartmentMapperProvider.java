package com.lin.yygh.hosp.mapper.provider;

import com.lin.yygh.model.base.BaseEntity;
import com.lin.yygh.model.hosp.v2.Department2;
import com.lin.yygh.vo.hosp.DepartmentQueryVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class DepartmentMapperProvider {

    public String getByHoscodeAndDepcode(Map<String, Object> params){
        String hoscode = ((String) params.get("hoscode"));
        String depcode = ((String) params.get("depcode"));

        StringBuilder sql = new StringBuilder();
        sql.append("select hospital_id, depname, intro, big_id from department ");
        sql.append(" where is_deleted = ").append(BaseEntity.STATE_SURVIVE);
        if (StringUtils.isNotBlank(hoscode)) {
            sql.append(" and hoscode = #{hoscode} ");
        }
        if (StringUtils.isNotBlank(depcode)) {
            sql.append(" and depcode = #{depcode} ");
        }
        return sql.toString();
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


    public String insertOrUpdateByHoscodeAndDepcode(Map<String, Object> params){
        Department2 department = (Department2) params.get("department");

        StringBuilder sql = new StringBuilder();
        sql.append(" insert into department (hospital_id, depcode, depname, intro) ");
        sql.append(" value (#{department.hospitalId}, {department.depcode}, {department.depname}, #{department.intro})");
        sql.append(" on duplicate key update");
        if(StringUtils.isNotBlank(department.getHospitalId())){
            sql.append(" set hospitalId = #{department.hospitalId}");
        }
        if(StringUtils.isNotBlank(department.getDepcode())){
            sql.append(" set depcode = #{department.depcode}");
        }
        if(StringUtils.isNotBlank(department.getDepname())){
            sql.append(" set depname = #{department.depname}");
        }
        if(StringUtils.isNotBlank(department.getIntro())){
            sql.append(" set intro = #{department.intro}");
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
