package com.lin.yygh.hosp.service.v2;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.yygh.model.hosp.Schedule;
import com.lin.yygh.model.hosp.v2.Hospital2;
import com.lin.yygh.model.hosp.v2.Schedule2;
import com.lin.yygh.vo.base.PageVo;
import com.lin.yygh.vo.hosp.ScheduleOrderVo;
import com.lin.yygh.vo.hosp.ScheduleQueryVo;

import java.util.List;
import java.util.Map;

public interface ScheduleService2 extends IService<Schedule2> {
    PageVo<Schedule2> findPageSchedule(ScheduleQueryVo scheduleQueryVo);

    public Map<String, Object> getRuleSchedule(long page, long limit, Long hoscode, Long depcode)

    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);


    Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);


    ScheduleOrderVo getScheduleOrderVo(String scheduleId);
}
