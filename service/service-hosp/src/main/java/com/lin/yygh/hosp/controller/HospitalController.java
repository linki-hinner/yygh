package com.lin.yygh.hosp.controller;

import com.lin.yygh.common.result.Result;
import com.lin.yygh.hosp.service.HospitalService;
import com.lin.yygh.model.hosp.Hospital;
import com.lin.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "医院管理")
public class HospitalController {
    private final HospitalService hospitalService;

    @ApiOperation("医院列表（条件查询分页）")
    @GetMapping("/admin/hosp/hospital/list/{page}/{limit}")
    public Result<Page<Hospital>> listHosp(@PathVariable Integer page,
                                           @PathVariable Integer limit,
                                           HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageModel = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("更新医院的上线状态")
    @GetMapping("/admin/hosp/hospital/updateHospStatus/{id}/{status}")
    public Result<?> updateHospStatus(@PathVariable String id,
                                      @PathVariable Integer status) {
        return hospitalService.updateStatus(id, status) ? Result.ok() : Result.fail();
    }

    @ApiOperation("医院详情信息")
    @GetMapping("/admin/hosp/hospital/showHospDetail/{id}")
    public Result<Hospital> showHospDetail(@PathVariable String id){
        Hospital hospital = hospitalService.show(id);
        return Result.ok(hospital);
    }

}
