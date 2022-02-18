package com.lin.yygh.hosp.controller;

import com.lin.yygh.common.result.Result;
import com.lin.yygh.hosp.service.ScheduleService;
import com.lin.yygh.model.hosp.Schedule;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
//@GetMapping("/admin/hosp/schedule/")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @ApiOperation(value = "查询排班规则数据")
    @GetMapping("/admin/hosp/schedule/getScheduleRule/{page}/{limit}/{depcode}/{hoscode}")
    public Result<?> getScheduleRule(@PathVariable Integer page,
                                     @PathVariable Integer limit,
                                     @PathVariable String depcode,
                                     @PathVariable String hoscode) {
        Map<String, Object> map= scheduleService.getRuleSchedule(page, limit, depcode, hoscode);
        return Result.ok(map);
    }

    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("/admin/hosp/schedule/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail( @PathVariable String hoscode,
                                     @PathVariable String depcode,
                                     @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    }
}
