package com.lin.yygh.hosp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.hosp.mapper.provider.HospitalSetMapperProvider;
import com.lin.yygh.model.hosp.HospitalSet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface HospitalSetMapper extends BaseMapper<HospitalSet> {
    @SelectProvider(type = HospitalSetMapperProvider.class, method = "getOneByHoscode")
    HospitalSet getOneByHoscode(@Param("hoscode")String hoscode);
}
