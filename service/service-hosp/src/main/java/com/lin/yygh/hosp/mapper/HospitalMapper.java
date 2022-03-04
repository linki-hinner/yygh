package com.lin.yygh.hosp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.hosp.mapper.provider.DepartmentMapperProvider;
import com.lin.yygh.hosp.mapper.provider.HospitalMapperProvider;
import com.lin.yygh.model.hosp.v2.Hospital2;
import com.lin.yygh.vo.hosp.DepartmentQueryVo;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface HospitalMapper extends BaseMapper<Hospital2> {
    @SelectProvider(type = HospitalMapperProvider.class, method = "getByHoscode")
    Hospital2 getByHoscode(@Param("hoscode")String hoscode);

    @InsertProvider(type = HospitalMapperProvider.class, method = "insertOrUpdateByHoscode")
    void insertOrUpdateByHoscode(@Param("hospital") Hospital2 hospital);

    @SelectProvider(type = HospitalMapperProvider.class, method = "listByDepartmentQueryVo")
    List<Hospital2> listByDepartmentQueryVo(@Param("vo") DepartmentQueryVo departmentQueryVo);

    @SelectProvider(type = DepartmentMapperProvider.class, method = "countByDepartmentQueryVo")
    int countByDepartmentQueryVo(@Param("vo") DepartmentQueryVo departmentQueryVo);
}
