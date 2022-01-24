package com.lin.yygh.cmn.controller;

import com.lin.yygh.cmn.service.DictService;
import com.lin.yygh.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "数据字典接口")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class DictController {
    private final DictService dictService;

    @ApiIgnore
    @GetMapping("/admin/cmn/dict")
    public void get() {
    }

    @ApiOperation("导出数据字典接口")
    @GetMapping("/admin/cmn/dict/exportData")
    public void exportDict(HttpServletResponse response){
        dictService.exportDictData(response);
    }

    @PostMapping("/admin/cmn/dict/importData")
    public void importData(MultipartFile file){
        dictService.importDictData(file);
    }

    @ApiOperation(value = "根据数据id查询子数据列表")
    @ApiImplicitParam(name = "id", value = "数据字典id", paramType = "path", defaultValue = "1")
    @GetMapping("/admin/cmn/dictfindChildData/{id}")
    public Result<?> findChildData(@PathVariable Long id) {
        return Result.ok(dictService.findChildData(id));
    }
}
