package com.lin.yygh.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.model.user.Patient;
import com.lin.yygh.user.mapper.provider.PatientMapperProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface PatientMapper extends BaseMapper<Patient> {
    @SelectProvider(type = PatientMapperProvider.class, method = "ListByUserId")
    List<Patient> ListByUserId(@Param("userId") Long userId);
}
