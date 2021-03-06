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

        //???????????????????????????
        for (BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }

        //?????????????????????????????????
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList", bookingScheduleRuleVoList);
        result.put("total", total);

        //??????????????????
        String hosName = hospitalService.getHospName(hospitalId);
        //??????????????????
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname", hosName);
        result.put("baseMap", baseMap);

        return result;
    }

    @Override
    public Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {



        Map<String, Object> result = new HashMap<>();

        //??????????????????
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        if (null == hospital) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();

        //?????????????????????????????????
        IPage iPage = this.getListDate(page, limit, bookingRule);
        //????????????????????????
        List<Date> dateList = iPage.getRecords();
        //??????????????????????????????????????????
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode)
                .and("workDate").in(dateList);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")//????????????
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleVoList = aggregationResults.getMappedResults();
        //???????????????????????????

        //???????????? ???????????????ScheduleVo?????????????????????????????????BookingRuleVo
        Map<Date, BookingScheduleRuleVo> scheduleVoMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(scheduleVoList)) {
            scheduleVoMap = scheduleVoList.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate,
                    BookingScheduleRuleVo -> BookingScheduleRuleVo));
        }
        //???????????????????????????
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        for (int i = 0, len = dateList.size(); i < len; i++) {
            Date date = dateList.get(i);

            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);
            if (null == bookingScheduleRuleVo) { // ??????????????????????????????
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                //??????????????????
                bookingScheduleRuleVo.setDocCount(0);
                //?????????????????????  -1????????????
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);
            //?????????????????????????????????
            String dayOfWeek = this.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);

            //?????????????????????????????????????????????   ?????? 0????????? 1??????????????? -1????????????????????????
            if (i == len - 1 && page == iPage.getPages()) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            //??????????????????????????????????????? ????????????
            if (i == 0 && page == 1) {
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if (stopTime.isBeforeNow()) {
                    //????????????
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }

        //???????????????????????????
        result.put("bookingScheduleList", bookingScheduleRuleVoList);
        result.put("total", iPage.getTotal());
        //??????????????????
        Map<String, String> baseMap = new HashMap<>();
        //????????????
        baseMap.put("hosname", hospitalService.getHospName(hoscode));
        //??????
        Department department = departmentService.getDepartment(hoscode, depcode);
        //???????????????
        baseMap.put("bigname", department.getBigname());
        //????????????
        baseMap.put("depname", department.getDepname());
        //???
        baseMap.put("workDateString", new DateTime().toString("yyyy???MM???"));
        //????????????
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        //????????????
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        return result;
    }


    @Override
    public ScheduleOrderVo getScheduleOrderVo(Long scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        //????????????
        Schedule2 schedule = baseMapper.selectById(scheduleId);

        //????????????????????????
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

        //?????????????????????????????????????????????-1????????????0???
        int quitDay = bookingRule2List.get(0).getQuitDay();
        DateTime quitTime = this.getDateTime(new DateTime(schedule.getWorkDate()).plusDays(quitDay).toDate(), bookingRule2List.get(0).getQuitTime());
        scheduleOrderVo.setQuitTime(quitTime.toDate());

        //??????????????????
        DateTime startTime = this.getDateTime(new Date(), bookingRule2List.get(0).getReleaseTime());
        scheduleOrderVo.setStartTime(startTime.toDate());

        //??????????????????
        DateTime endTime = this.getDateTime(new DateTime().plusDays(bookingRule2List.get(0).getCycle()).toDate(), bookingRule2List.get(0).getStopTime());
        scheduleOrderVo.setEndTime(endTime.toDate());

        return scheduleOrderVo;
    }

    /**
     * ???Date?????????yyyy-MM-dd HH:mm????????????DateTime
     */
    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " " + timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }

    /**
     * ?????????????????????????????????
     */
    private IPage<Date> getListDate(int page, int limit, BookingRule bookingRule) {
        //??????????????????
        DateTime releaseTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        //????????????
        int cycle = bookingRule.getCycle();
        //??????????????????????????????????????????????????????????????????????????????????????????1
        if (releaseTime.isBeforeNow()) cycle += 1;
        //???????????????????????????????????????????????????????????????
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
            //????????????????????????
            DateTime curDateTime = new DateTime().plusDays(i);
            String dateString = curDateTime.toString("yyyy-MM-dd");
            dateList.add(new DateTime(dateString).toDate());
        }
        //?????????????????????????????????????????????????????????????????????7????????????????????????????????????
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
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "??????";
            default:
                break;
        }
        return dayOfWeek;
    }
}
