package com.lin.yygh.hosp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.hosp.mapper.provider.HospitalMapperProvider;
import com.lin.yygh.model.hosp.Hospital;
import com.lin.yygh.model.hosp.v2.Hospital2;
import com.lin.yygh.vo.hosp.HospitalQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

public interface HospitalMapper extends BaseMapper<Hospital2> {
    @SelectProvider(type = HospitalMapperProvider.class, method = "listByHospitalQueryVo")
    List<Hospital2> listByHospitalQueryVo(@Param("vo") HospitalQueryVo hospitalQueryVo);

    @SelectProvider(type = HospitalMapperProvider.class, method = "countByHospitalQueryVo")
    long countByHospitalQueryVo(@Param("vo")HospitalQueryVo hospitalQueryVo);

    @UpdateProvider(type = HospitalMapperProvider.class, method = "updateState")
    boolean updateState(@Param("id")Long hospitalId, @Param("status")Integer status);

    @SelectProvider(type = HospitalMapperProvider.class, method = "listByHosnameLike")
    List<Hospital> listByHosnameLike(String hosname);
}
