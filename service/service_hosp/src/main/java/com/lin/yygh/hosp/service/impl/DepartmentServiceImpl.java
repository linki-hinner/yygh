package com.lin.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lin.yygh.hosp.repository.DepartmentRepository;
import com.lin.yygh.hosp.service.DepartmentService;
import com.lin.yygh.model.hosp.Department;
import com.lin.yygh.vo.hosp.DepartmentQueryVo;
import com.lin.yygh.vo.hosp.DepartmentVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        String jsonString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(jsonString, Department.class);

        Department departmentDO = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (departmentDO == null) {
            department.setCreateTime(new Date());
        }
        department.setUpdateTime(new Date());
        department.setIsDeleted(0);
        departmentRepository.save(department);
    }

    @Override
    public Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {
        Pageable pageable = PageRequest.of(page - 1, limit);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Department department = new Department();
        department.setHoscode(departmentQueryVo.getHoscode());
        department.setIsDeleted(0);

        Example<Department> example = Example.of(department, matcher);

        return departmentRepository.findAll(example, pageable);
    }

    @Override
    public void remove(String hosCode, String depCode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hosCode, depCode);
        if (department != null) {
            departmentRepository.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hosCode) {
        List<DepartmentVo> departmentVos = new ArrayList<>();

        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hosCode);
        List<Department> all = departmentRepository.findAll(Example.of(departmentQuery));

        Map<String, List<Department>> collect =
                all.stream().collect(
                        Collectors.groupingBy(Department::getBigcode)
                );
        for (Map.Entry<String, List<Department>> entry : collect.entrySet()) {
            List<DepartmentVo> child = new ArrayList<>(20);
            for (Department department : entry.getValue()) {
                DepartmentVo departmentVoChild = new DepartmentVo();
                departmentVoChild.setDepcode(department.getDepcode());
                departmentVoChild.setDepname(department.getDepname());
                child.add(departmentVoChild);
            }

            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(entry.getKey());
            departmentVo.setDepname(entry.getValue().get(0).getBigname());
            departmentVo.setChildren(child);
            departmentVos.add(departmentVo);
        }

        return departmentVos;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department
                = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        return department == null ? null : department.getDepname();
    }

    @Override
    public Department getDepartment(String hoscode, String depcode) {
        return departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
    }
}
