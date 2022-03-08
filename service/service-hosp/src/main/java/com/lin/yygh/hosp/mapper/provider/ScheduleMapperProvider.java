package com.lin.yygh.hosp.mapper.provider;

import com.lin.yygh.model.base.BaseEntity;
import com.lin.yygh.vo.hosp.ScheduleQueryVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class ScheduleMapperProvider {
    public String listByScheduleQueryVo(Map<String, Object> params){
        ScheduleQueryVo vo = (ScheduleQueryVo) params.get("vo");

        StringBuilder sql = new StringBuilder();
        sql.append("select * from schedule ").append(where(vo));

        int current = ObjectUtils.firstNonNull(vo.getCurrent(), 1);
        int size = ObjectUtils.firstNonNull(vo.getSize(), 10);
        sql.append(" limit ").append((current - 1) * size);
        sql.append(" , ").append(size);

        return sql.toString();
    }

    public String countByScheduleQueryVo(Map<String, Object> params){
        ScheduleQueryVo vo = (ScheduleQueryVo) params.get("vo");
        return "select count(*) from schedule " + where(vo);
    }

    public String CountBookingScheduleRuleVo(Map<String, Object> params){
        long current = (long) params.get("current");
        long size = (long) params.get("size");

        return " select count(distinct work_date) " +
                " from schedule " +
                " where is_deleted = " + BaseEntity.STATE_DELETED +
                " and hospital_id = #{hospitalId} " +
                " and department_id = #{departmentId} ";
    }

    public String ListBookingScheduleRuleVo(Map<String, Object> params){
        long current = (long) params.get("current");
        long size = (long) params.get("size");

        return "select work_date workDate, count(*) docCount, sum(reserved_number) reservedNumber, sum(available_number) availableNumber " +
                " from schedule " +
                " where is_deleted = " + BaseEntity.STATE_DELETED +
                " and hospital_id = #{hospitalId} " +
                " and department_id = #{departmentId} " +
                " group by work_date " +
                " order by work_date desc " +
                " limit " + (current - 1) * size +
                " , " + size;
    }

    private String where(ScheduleQueryVo vo){
        StringBuilder sql = new StringBuilder();
        sql.append(" where is_deleted = ").append(BaseEntity.STATE_SURVIVE);
        if (null != vo.getHospitalId()) {
            sql.append(" and hospital_id = #{vo.hospitalId} ");
        }
        if(null == vo.getDepartmentId()){
            sql.append(" and department_id = #{vo.departmentId} ");
        }
        if (StringUtils.isNotBlank(vo.getDoccode())) {
            sql.append(" and doccode = #{vo.doccode} ");
        }
        if (null != vo.getWorkDate()) {
            sql.append(" and work_date = #{vo.work_date} ");
        }
        if (null != vo.getWorkTime()) {
            sql.append(" and work_time = #{vo.work_time} ");
        }
        return sql.toString();
    }
}
