package com.lin.yygh.hosp.service.impl.v2;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.yygh.common.exception.YyghException;
import com.lin.yygh.common.result.ResultCodeEnum;
import com.lin.yygh.hosp.mapper.ScheduleMapper;
import com.lin.yygh.hosp.service.v2.BookingRuleService;
import com.lin.yygh.hosp.service.v2.DepartmentService2;
import com.lin.yygh.hosp.service.v2.HospitalService2;
import com.lin.yygh.hosp.service.v2.ScheduleService2;
import com.lin.yygh.model.hosp.BookingRule;
import com.lin.yygh.model.hosp.Department;
import com.lin.yygh.model.hosp.Hospital;
import com.lin.yygh.model.hosp.Schedule;
import com.lin.yygh.model.hosp.v2.BookingRule2;
import com.lin.yygh.model.hosp.v2.Hospital2;
import com.lin.yygh.model.hosp.v2.Schedule2;
import com.lin.yygh.vo.base.PageVo;
import com.lin.yygh.vo.hosp.BookingScheduleRuleVo;
import com.lin.yygh.vo.hosp.ScheduleOrderVo;
import com.lin.yygh.vo.hosp.ScheduleQueryVo;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl2 extends ServiceImpl<ScheduleMapper, Schedule2> implements ScheduleService2 {
    private final HospitalService2 hospitalService;
    private final DepartmentService2 departmentService2;
    private final BookingRuleService bookingRuleService;

    @Override
    public PageVo<Schedule2> findPageSchedule(ScheduleQueryVo scheduleQueryVo) {
        List<Schedule2> hospital2List = baseMapper.listByScheduleQueryVo(scheduleQueryVo);
        long total = baseMapper.countByScheduleQueryVo(scheduleQueryVo);
        PageVo<Schedule2> page = new PageVo<>();
        page.setData(hospital2List);
        page.setTotal(total);
        return page;
    }

    @Override
    public List<Schedule2> getDetailSchedule(Long hospitalId, Long departmentId, Date workDate) {
        ScheduleQueryVo scheduleOrderVo = new ScheduleQueryVo();
        scheduleOrderVo.setHospitalId(hospitalId);
        scheduleOrderVo.setDepartmentId(departmentId);
        scheduleOrderVo.setWorkDate(workDate);
        List<Schedule2> schedule2List = baseMapper.listByScheduleQueryVo(scheduleOrderVo);

        schedule2List.forEach(this::packageSchedule);
        return schedule2List;
    }

    @Override
    public Map<String, Object> getRuleSchedule(long page, long limit, Long hospitalId, Long departmentId) {
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList =
                baseMapper.ListBookingScheduleRuleVo(page, limit, hospitalId, departmentId);
        Long total =
                baseMapper.countBookingScheduleRuleVo(page, limit, hospitalId, departmentId);

        //把日期对应星期获取
        for (BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }

        //设置最终数据，进行返回
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList", bookingScheduleRuleVoList);
        result.put("total", total);

        //获取医院名称
        String hosName = hospitalService.getHospName(hospitalId);
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname", hosName);
        result.put("baseMap", baseMap);

        return result;
    }

    @Override
    public Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {



        Map<String, Object> result = new HashMap<>();

        //获取预约规则
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        if (null == hospital) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();

        //获取可预约日期分页数据
        IPage iPage = this.getListDate(page, limit, bookingRule);
        //当前页可预约日期
        List<Date> dateList = iPage.getRecords();
        //获取可预约日期科室剩余预约数
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode)
                .and("workDate").in(dateList);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")//分组字段
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleVoList = aggregationResults.getMappedResults();
        //获取科室剩余预约数

        //合并数据 将统计数据ScheduleVo根据“安排日期”合并到BookingRuleVo
        Map<Date, BookingScheduleRuleVo> scheduleVoMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(scheduleVoList)) {
            scheduleVoMap = scheduleVoList.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate,
                    BookingScheduleRuleVo -> BookingScheduleRuleVo));
        }
        //获取可预约排班规则
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        for (int i = 0, len = dateList.size(); i < len; i++) {
            Date date = dateList.get(i);

            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);
            if (null == bookingScheduleRuleVo) { // 说明当天没有排班医生
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                //就诊医生人数
                bookingScheduleRuleVo.setDocCount(0);
                //科室剩余预约数  -1表示无号
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);
            //计算当前预约日期为周几
            String dayOfWeek = this.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);

            //最后一页最后一条记录为即将预约   状态 0：正常 1：即将放号 -1：当天已停止挂号
            if (i == len - 1 && page == iPage.getPages()) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            //当天预约如果过了停号时间， 不能预约
            if (i == 0 && page == 1) {
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if (stopTime.isBeforeNow()) {
                    //停止预约
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }

        //可预约日期规则数据
        result.put("bookingScheduleList", bookingScheduleRuleVoList);
        result.put("total", iPage.getTotal());
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        //医院名称
        baseMap.put("hosname", hospitalService.getHospName(hoscode));
        //科室
        Department department = departmentService.getDepartment(hoscode, depcode);
        //大科室名称
        baseMap.put("bigname", department.getBigname());
        //科室名称
        baseMap.put("depname", department.getDepname());
        //月
        baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
        //放号时间
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        //停号时间
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        return result;
    }


    @Override
    public ScheduleOrderVo getScheduleOrderVo(Long scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        //排班信息
        Schedule2 schedule = baseMapper.selectById(scheduleId);

        //获取预约规则信息
        Hospital2 hospital = hospitalService.getById(schedule.getHospitalId());
        List<BookingRule2> bookingRule2List = bookingRuleService.listByHospitalId(schedule.getHospitalId());

        scheduleOrderVo.setHosname(hospitalService.getHospName(schedule.getHospitalId()));
        scheduleOrderVo.setDepcode(schedule.getDepartmentId().toString());
        scheduleOrderVo.setDepname(departmentService2.getDepName(schedule.getDepartmentId()));
        scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId());
        scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber());
        scheduleOrderVo.setTitle(schedule.getTitle());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());
        scheduleOrderVo.setAmount(schedule.getAmount());

        //退号截止天数（如：就诊前一天为-1，当天为0）
        int quitDay = bookingRule2List.get(0).getQuitDay();
        DateTime quitTime = this.getDateTime(new DateTime(schedule.getWorkDate()).plusDays(quitDay).toDate(), bookingRule2List.get(0).getQuitTime());
        scheduleOrderVo.setQuitTime(quitTime.toDate());

        //预约开始时间
        DateTime startTime = this.getDateTime(new Date(), bookingRule2List.get(0).getReleaseTime());
        scheduleOrderVo.setStartTime(startTime.toDate());

        //预约截止时间
        DateTime endTime = this.getDateTime(new DateTime().plusDays(bookingRule2List.get(0).getCycle()).toDate(), bookingRule2List.get(0).getStopTime());
        scheduleOrderVo.setEndTime(endTime.toDate());

        return scheduleOrderVo;
    }

    /**
     * 将Date日期（yyyy-MM-dd HH:mm）转换为DateTime
     */
    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " " + timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }

    /**
     * 获取可预约日期分页数据
     */
    private IPage<Date> getListDate(int page, int limit, BookingRule bookingRule) {
        //当天放号时间
        DateTime releaseTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        //预约周期
        int cycle = bookingRule.getCycle();
        //如果当天放号时间已过，则预约周期后一天为即将放号时间，周期加1
        if (releaseTime.isBeforeNow()) cycle += 1;
        //可预约所有日期，最后一天显示即将放号倒计时
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
            //计算当前预约日期
            DateTime curDateTime = new DateTime().plusDays(i);
            String dateString = curDateTime.toString("yyyy-MM-dd");
            dateList.add(new DateTime(dateString).toDate());
        }
        //日期分页，由于预约周期不一样，页面一排最多显示7天数据，多了就要分页显示
        List<Date> pageDateList = new ArrayList<>();
        int start = (page - 1) * limit;
        int end = (page - 1) * limit + limit;
        if (end > dateList.size()) end = dateList.size();
        for (int i = start; i < end; i++) {
            pageDateList.add(dateList.get(i));
        }
        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page(page, 7, dateList.size());
        iPage.setRecords(pageDateList);
        return iPage;
    }

    private void packageSchedule(Schedule2 schedule) {
        Map<String, Object> param = schedule.getParam();
        param.put("hosname", hospitalService.getHospName(schedule.getHospitalId()));
        param.put("depname", departmentService2.getDepName(schedule.getDepartmentId()));
        param.put("dayOfWeek", this.getDayOfWeek(new DateTime(schedule.getWorkDate())));
    }

    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }
}
