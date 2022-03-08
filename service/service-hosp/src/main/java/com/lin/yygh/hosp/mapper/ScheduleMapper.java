package com.lin.yygh.hosp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.hosp.mapper.provider.ScheduleMapperProvider;
import com.lin.yygh.model.hosp.v2.Schedule2;
import com.lin.yygh.vo.hosp.BookingScheduleRuleVo;
import com.lin.yygh.vo.hosp.ScheduleQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface ScheduleMapper extends BaseMapper<Schedule2> {
    @SelectProvider(type = ScheduleMapperProvider.class, method = "listByScheduleQueryVo")
    List<Schedule2> listByScheduleQueryVo(@Param("vo") ScheduleQueryVo scheduleQueryVo);

    @SelectProvider(type = ScheduleMapperProvider.class, method = "countByScheduleQueryVo")
    long countByScheduleQueryVo(@Param("vo")ScheduleQueryVo scheduleQueryVo);

    @SelectProvider(type = ScheduleMapperProvider.class, method = "ListBookingScheduleRuleVo")
    List<BookingScheduleRuleVo> ListBookingScheduleRuleVo(@Param("current") Long current,
                                                    @Param("size") Long size,
                                                    @Param("hospitalId") Long hospitalId,
                                                    @Param("departmentId") Long departmentId);

    @SelectProvider(type = ScheduleMapperProvider.class, method = "CountBookingScheduleRuleVo")
    Long countBookingScheduleRuleVo(@Param("current") Long current,
                                                    @Param("size") Long size,
                                                    @Param("hospitalId") Long hospitalId,
                                                    @Param("departmentId") Long departmentId);

}
