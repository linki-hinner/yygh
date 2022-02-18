package com.lin.yygh.cmn.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-cmn")
@Service
public interface DictFeignClient {
    @ApiOperation("根据ditcode和value查询")
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    String getName(@PathVariable("dictCode") String dictCode,
                   @PathVariable("value") String value);

    @ApiOperation("根据value查询")
    @GetMapping("/admin/cmn/dict/getName/{value}")
    String getName(@PathVariable("value") String value);
}
