package com.lin.yygh.hosp.service.v2;

import com.lin.yygh.model.hosp.v2.Department2;
import com.lin.yygh.vo.hosp.DepartmentQueryVo;
import com.lin.yygh.vo.hosp.DepartmentVo;

import java.util.List;
import java.util.Map;

public interface DepartmentService2 {
    void save(Map<String, Object> paramMap);

    Map<String, Object> findPageDepartment(DepartmentQueryVo departmentQueryVo);

    void remove(String hosCode, String depCode);

    List<DepartmentVo> findDeptTree(String hosCode);

    String getDepName(String hoscode, String depcode);

    Department2 getDepartment(String hoscode, String depcode);
}
