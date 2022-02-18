package com.lin.yygh.hosp;


import com.lin.yygh.vo.hosp.ScheduleOrderVo;
import com.lin.yygh.vo.order.SignInfoVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-hosp")
@Repository
public interface HospitalFeignClient{
    @ApiOperation(value = "根据排班id获取预约下单数据")
    @GetMapping("inner/getScheduleOrderVo/{scheduleId}")
    ScheduleOrderVo getScheduleOrderVo(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable("scheduleId") String scheduleId);

    @ApiOperation(value = "获取医院签名信息")
    @GetMapping("/admin/hosp/hospitalSet/inner/getSignInfoVo/{hoscode}")
    SignInfoVo getSignInfoVo(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable("hoscode") String hoscode);
}