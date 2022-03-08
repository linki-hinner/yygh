package com.lin.yygh.hosp.mapper.provider;

import com.lin.yygh.model.base.BaseEntity;
import com.lin.yygh.vo.hosp.HospitalQueryVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class HospitalMapperProvider {
    public String listByHospitalQueryVo(Map<String, Object> params){
        HospitalQueryVo vo = (HospitalQueryVo) params.get("vo");

        StringBuilder sql = new StringBuilder();
        sql.append("select * from hospital ").append(where(vo));

        int current = ObjectUtils.firstNonNull(vo.getCurrent(), 1);
        int size = ObjectUtils.firstNonNull(vo.getSize(), 10);
        sql.append(" limit ").append((current - 1) * size);
        sql.append(" , ").append(size);

        return sql.toString();
    }

    public String countByHospitalQueryVo(Map<String, Object> params){
        HospitalQueryVo vo = (HospitalQueryVo) params.get("vo");
        return "select count(*) from hospital " + where(vo);
    }

    public String updateState(Map<String, Object> params){
        Long id = (Long) params.get("id");
        Integer status = (Integer) params.get("status");

        return "update hospital set status = #{status} where id = #{id}";
    }

    public String listByHosnameLike(Map<String, Object> params){
        return "select (*) from hospital " + " where is_deleted = " + BaseEntity.STATE_SURVIVE +
                " and hospital like CONCAT('%', #{hosname},'%')";
    }

    private String where(HospitalQueryVo vo){
        StringBuilder sql = new StringBuilder();
        sql.append(" where is_deleted = ").append(BaseEntity.STATE_SURVIVE);
        if (null != vo.getHospitalId()) {
            sql.append(" and ho = #{vo.hospitalId} ");
        }
        if (StringUtils.isNotBlank(vo.getHosname())) {
            sql.append(" and hosname = #{vo.hosname} ");
        }
        if (StringUtils.isNotBlank(vo.getHostype())) {
            sql.append(" and hostype = #{vo.hostype} ");
        }
        if (StringUtils.isNotBlank(vo.getProvinceCode())) {
            sql.append(" and province_code = #{vo.provinceCode} ");
        }
        if (StringUtils.isNotBlank(vo.getCityCode())) {
            sql.append(" and city_code = #{vo.cityCode} ");
        }
        if (StringUtils.isNotBlank(vo.getDistrictCode())) {
            sql.append(" and district_code = #{vo.districtCode} ");
        }
        if (null != vo.getStatus()) {
            sql.append(" and status = #{vo.status} ");
        }
        return sql.toString();
    }
}
