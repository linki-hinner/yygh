package com.lin.yygh.hosp.service;

import com.lin.yygh.model.hosp.Department;
import com.lin.yygh.vo.hosp.DepartmentQueryVo;
import com.lin.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> paramMap);

    Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hosCode, String depCode);

    List<DepartmentVo> findDeptTree(String hosCode);

    String getDepName(String hoscode, String depcode);

    Department getDepartment(String hoscode, String depcode);
}
