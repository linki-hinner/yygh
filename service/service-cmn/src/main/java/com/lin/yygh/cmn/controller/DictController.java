package com.lin.yygh.cmn.controller;

import com.lin.yygh.cmn.service.DictService;
import com.lin.yygh.common.result.Result;
import com.lin.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "数据字典接口")
@RestController
@RequiredArgsConstructor
public class DictController {
    private final DictService dictService;

    @ApiOperation("导出数据字典接口")
    @GetMapping("/admin/cmn/dict/exportData")
    public void exportDict(HttpServletResponse response) {
        dictService.exportDictData(response);
    }

    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/admin/cmn/dict/findByDictCode/{dictCode}")
    public Result<List<Dict>> findByDictCode(
            @ApiParam(defaultValue = "Province", name = "dictCode", value = "节点编码", required = true)
            @PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

    @PostMapping("/admin/cmn/dict/importData")
    public void importData(MultipartFile file) {
        dictService.importDictData(file);
    }

    @ApiOperation(value = "根据数据id查询子数据列表")
    @ApiImplicitParam(name = "id", value = "数据字典id", paramType = "path", defaultValue = "1")
    @GetMapping("/admin/cmn/dict/findChildData/{id}")
    public Result<?> findChildData(@PathVariable Long id) {
        return Result.ok(dictService.setChildData(id));
    }

    @ApiOperation("根据dicode和value查询")
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value){
        return dictService.getDictName(dictCode, value);
    }

    @ApiOperation("根据value查询")
    @GetMapping("/admin/cmn/dict/getName/{value}")
    public String getName(@PathVariable String value){
        return dictService.getDictName("", value);
    }
}
