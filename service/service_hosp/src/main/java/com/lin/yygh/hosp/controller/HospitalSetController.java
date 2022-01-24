package com.lin.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.yygh.common.result.Result;
import com.lin.yygh.common.util.MD5;
import com.lin.yygh.hosp.service.HospitalSetService;
import com.lin.yygh.model.hosp.HospitalSet;
import com.lin.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class HospitalSetController {
    private final HospitalSetService hospitalSetService;
    private final Random random = new Random();

    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("/admin/hosp/hospitalSet/findAll")
    public Result<List<HospitalSet>> findAllHospitalSet() {
        //调用service的方法
        return Result.ok(hospitalSetService.list());
    }

    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("/admin/hosp/hospitalSet/{id}")
    public Result<?> removeHospSet(@PathVariable Long id) {
        return hospitalSetService.removeById(id) ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "条件查询带分页")
    @PostMapping("/admin/hosp/hospitalSet/findPage/{current}/{limit}")
    public Result<Page<HospitalSet>> findPageHospSet(@PathVariable long current,
                                                     @PathVariable long limit,
                                                     @RequestBody(required = false) HospitalQueryVo hospitalQueryVo) {
        Page<HospitalSet> page = new Page<>(current, limit);
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String str;
        if (!StringUtils.isEmpty(str = hospitalQueryVo.getHosname())) {
            wrapper.like("hosname", str);
        }
        if (!StringUtils.isEmpty(str = hospitalQueryVo.getHoscode())) {
            wrapper.eq("hoscode", hospitalQueryVo.getHoscode());
        }

        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);

        return Result.ok(pageHospitalSet);
    }

    @ApiOperation("添加医院设置")
    @PostMapping("/admin/hosp/hospitalSet/saveHospitalSet")
    public Result<?> saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        hospitalSet.setId(null);
        hospitalSet.setStatus(1);
        hospitalSet.setSignKey(
                MD5.encrypt(System.currentTimeMillis()
                        + ""
                        + this.random.nextInt(10000))
        );

        return hospitalSetService.save(hospitalSet) ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据id获取医院设置")
    @GetMapping("/admin/hosp/hospitalSet/getHospSet/{id}")
    public Result<HospitalSet> getHospSet(@PathVariable Long id) {
        return Result.ok(hospitalSetService.getById(id));
    }

    @ApiOperation("修改医院设置")
    @PostMapping("/admin/hosp/hospitalSet/updateHospitalSet")
    public Result<?> updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        return hospitalSetService.updateById(hospitalSet) ? Result.ok() : Result.fail();
    }

    @ApiOperation("批量删除医院设置")
    @DeleteMapping("/admin/hosp/hospitalSet/batchRemove")
    public Result<?> batchRemove(@RequestBody List<Long> idList) {
        return hospitalSetService.removeByIds(idList) ? Result.ok() : Result.fail();
    }

    @ApiOperation("锁定医院")
    @PutMapping("/admin/hosp/hospitalSet/lockHospitalSet/{id}/{status}")
    public Result<?> lockHospitalSet(@PathVariable String id,
                                     @PathVariable Integer status) {
        HospitalSet byId = hospitalSetService.getById(id);

        byId.setStatus(status);

        hospitalSetService.updateById(byId);

        return Result.ok();
    }

    @ApiOperation("发送签名密钥")
    @PutMapping("sendKey/{id}")
    public Result<?> sendKey(@PathVariable Long id) {
        HospitalSet byId = hospitalSetService.getById(id);
        String signKey = byId.getSignKey();
        String hoscode = byId.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }
}
