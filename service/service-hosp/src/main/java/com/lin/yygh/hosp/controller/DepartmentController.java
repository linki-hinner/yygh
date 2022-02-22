package com.lin.yygh.hosp.controller;

import com.lin.yygh.common.result.Result;
import com.lin.yygh.hosp.service.DepartmentService;
import com.lin.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @ApiOperation("查询医院所有科室列表")
    @GetMapping("/admin/hosp/department/getDeptList/{hoscode}")
    public Result<List<DepartmentVo>> getDeptList(@PathVariable String hoscode) {
        List<DepartmentVo> departmentVoList = departmentService.findDeptTree(hoscode);
        return Result.ok(departmentVoList);
    }
}
