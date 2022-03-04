package com.lin.yygh.hosp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.hosp.mapper.provider.DepartmentMapperProvider;
import com.lin.yygh.model.hosp.v2.Department2;
import com.lin.yygh.vo.hosp.DepartmentQueryVo;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface DepartmentMapper extends BaseMapper<Department2> {
    @SelectProvider(type = DepartmentMapperProvider.class, method = "getByHoscodeAndDepcode")
    Department2 getByHoscodeAndDepcode(@Param("hoscode")String hoscode, @Param("depcode")String depcode);

    @InsertProvider(type = DepartmentMapperProvider.class, method = "insertOrUpdateByHoscodeAndDepcode")
    void insertOrUpdateByHoscodeAndDepcode(@Param("department") Department2 department);

    @SelectProvider(type = DepartmentMapperProvider.class, method = "listByDepartmentQueryVo")
    List<Department2> listByDepartmentQueryVo(@Param("vo") DepartmentQueryVo departmentQueryVo);

    @SelectProvider(type = DepartmentMapperProvider.class, method = "listByHoscode")
    List<Department2> listByHoscode(@Param("hoscode") String hoscode);

    @SelectProvider(type = DepartmentMapperProvider.class, method = "countByDepartmentQueryVo")
    int countByDepartmentQueryVo(@Param("vo") DepartmentQueryVo departmentQueryVo);
}
