package com.lin.yygh.hosp.service.impl.v2;

import com.alibaba.fastjson.JSONObject;
import com.lin.yygh.hosp.mapper.DepartmentMapper;
import com.lin.yygh.hosp.service.v2.DepartmentService2;
import com.lin.yygh.model.hosp.v2.Department2;
import com.lin.yygh.vo.hosp.DepartmentQueryVo;
import com.lin.yygh.vo.hosp.DepartmentVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl2 implements DepartmentService2 {
    private final DepartmentMapper departmentMapper;

    @Override
    public void save(Map<String, Object> paramMap) {
        String jsonString = JSONObject.toJSONString(paramMap);
        Department2 department = JSONObject.parseObject(jsonString, Department2.class);
        departmentMapper.insertOrUpdateByHoscodeAndDepcode(department);
    }

    @Override
    public Map<String, Object> findPageDepartment(DepartmentQueryVo departmentQueryVo) {
        List<Department2> department2s = departmentMapper.listByDepartmentQueryVo(departmentQueryVo);
        int total = departmentMapper.countByDepartmentQueryVo(departmentQueryVo);

        Map<String, Object> result = new HashMap<>();
        result.put("totalElements", total);
        result.put("pageNum", departmentQueryVo.getCurrent());
        result.put("content", department2s);

        return result;
    }

    @Override
    public void remove(String hosCode, String depCode) {
        Department2 department = departmentMapper.getByHoscodeAndDepcode(hosCode, depCode);
        if (department != null) {
            departmentMapper.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hosCode) {
        List<Department2> all = departmentMapper.listByHoscode(hosCode);

        List<DepartmentVo> departmentVos = new ArrayList<>();

        Map<Integer, List<Department2>> collect =
                all.stream().collect(
                        Collectors.groupingBy(Department2::getBigId)
                );
        for (Map.Entry<Integer, List<Department2>> entry : collect.entrySet()) {
            List<DepartmentVo> child = new ArrayList<>(20);
            for (Department2 department : entry.getValue()) {
                DepartmentVo departmentVoChild = new DepartmentVo();
                departmentVoChild.setDepcode(department.getDepcode());
                departmentVoChild.setDepname(department.getDepname());
                child.add(departmentVoChild);
            }

            Department2 father = departmentMapper.selectById(entry.getKey());
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(father.getDepcode());
            departmentVo.setDepname(father.getDepname());
            departmentVo.setChildren(child);
            departmentVos.add(departmentVo);
        }

        return departmentVos;
    }

    @Override
    public String getDepName(Long depcode) {
        Department2 department = departmentMapper.getByHoscodeAndDepcode(hoscode, depcode);
        return department == null ? null : department.getDepname();
    }

    @Override
    public Department2 getDepartment(String hoscode, String depcode) {
        return departmentMapper.getByHoscodeAndDepcode(hoscode, depcode);
    }
}
