package com.lin.yygh.hosp.controller.api;

import com.lin.yygh.common.helper.HttpRequestHelper;
import com.lin.yygh.common.result.Result;
import com.lin.yygh.common.result.ResultCodeEnum;
import com.lin.yygh.common.util.MD5;
import com.lin.yygh.hosp.service.DepartmentService;
import com.lin.yygh.hosp.service.HospitalService;
import com.lin.yygh.hosp.service.HospitalSetService;
import com.lin.yygh.hosp.service.ScheduleService;
import com.lin.yygh.model.hosp.HospitalSet;
import com.lin.yygh.vo.hosp.DepartmentQueryVo;
import com.lin.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RestController
//@RequestMapping("/api/hosp")
@RequiredArgsConstructor
public class ApiController {
    private final HospitalService hospitalService;
    private final HospitalSetService hospitalSetService;
    private final DepartmentService departmentService;
    private final ScheduleService scheduleService;

    @ApiOperation("查询医院接口")
    @PostMapping("/api/hosp/hospital/show")
    public Result<?> getHospital(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hospSign = (String) paramMap.get("sign");
        String hosCode = (String) paramMap.get("hoscode");

        Optional<HospitalSet> hospitalSetByHoscode = hospitalSetService.getHospitalSetByHoscode(hosCode);

        String signKeyMD5 = hospitalSetByHoscode
                .map(HospitalSet::getSignKey)
                .map(MD5::encrypt)
                .orElseGet(() -> null);

        if (!hospSign.equals(signKeyMD5)) {
            return Result.build(null, ResultCodeEnum.SIGN_ERROR);
        }

        return Result.ok(hospitalService.getByHoscode(hosCode));
    }

    @ApiOperation("上传医院接口")
    @PostMapping("/api/hosp/saveHospital")
    public Result<?> saveHosp(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hospSign = (String) paramMap.get("sign");
        String hosCode = (String) paramMap.get("hoscode");

        Optional<HospitalSet> hospitalSetByHoscode = hospitalSetService.getHospitalSetByHoscode(hosCode);

        String signKeyMD5 = hospitalSetByHoscode
                .map(HospitalSet::getSignKey)
                .map(MD5::encrypt)
                .orElseGet(() -> null);

        if (!hospSign.equals(signKeyMD5)) {
            return Result.build(null, ResultCodeEnum.SIGN_ERROR);
        }

        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        paramMap.put("logoData", logoData);
        hospitalService.save(paramMap);
        return Result.ok();
    }


    @ApiOperation("上传科室接口")
    @PostMapping("/api/hosp/saveDepartment")
    public Result<?> saveDepartment(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hospSign = (String) paramMap.get("sign");
        String hosCode = (String) paramMap.get("hoscode");

        Optional<HospitalSet> hospitalSetByHoscode = hospitalSetService.getHospitalSetByHoscode(hosCode);

        String signKeyMD5 = hospitalSetByHoscode
                .map(HospitalSet::getSignKey)
                .map(MD5::encrypt)
                .orElseGet(() -> null);

        if (!hospSign.equals(signKeyMD5)) {
            return Result.build(null, ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation("查询科室接口")
    @PostMapping("/api/hosp/department/list")
    public Result<?> findDepartment(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hospSign = (String) paramMap.get("sign");
        String hosCode = (String) paramMap.get("hoscode");

        Optional<HospitalSet> hospitalSetByHoscode = hospitalSetService.getHospitalSetByHoscode(hosCode);

        String signKeyMD5 = hospitalSetByHoscode
                .map(HospitalSet::getSignKey)
                .map(MD5::encrypt)
                .orElseGet(() -> null);

        if (!hospSign.equals(signKeyMD5)) {
            return Result.build(null, ResultCodeEnum.SIGN_ERROR);
        }


        Integer page = paramMap.get("page") == null ? 1 : Integer.parseInt((String) paramMap.get("page"));
        Integer limit = paramMap.get("limit") == null ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hosCode);

        return Result.ok(departmentService.findPageDepartment(page, limit, departmentQueryVo));
    }

    @ApiOperation("科室删除接口")
    @PostMapping("/api/hosp/department/remove")
    public Result<?> removeDepartment(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hospSign = (String) paramMap.get("sign");
        String hosCode = (String) paramMap.get("hoscode");

        Optional<HospitalSet> hospitalSetByHoscode = hospitalSetService.getHospitalSetByHoscode(hosCode);

        String signKeyMD5 = hospitalSetByHoscode
                .map(HospitalSet::getSignKey)
                .map(MD5::encrypt)
                .orElseGet(() -> null);

        if (!hospSign.equals(signKeyMD5)) {
            return Result.build(null, ResultCodeEnum.SIGN_ERROR);
        }

        String depCode = (String) paramMap.get("depcode");

        departmentService.remove(hosCode, depCode);
        return Result.ok();
    }

    @ApiOperation("上传排班接口")
    @PostMapping("/api/hosp/saveSchedule")
    public Result<?> saveSchedule(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hospSign = (String) paramMap.get("sign");
        String hosCode = (String) paramMap.get("hoscode");

        Optional<HospitalSet> hospitalSetByHoscode = hospitalSetService.getHospitalSetByHoscode(hosCode);

        String signKeyMD5 = hospitalSetByHoscode
                .map(HospitalSet::getSignKey)
                .map(MD5::encrypt)
                .orElseGet(() -> null);

        if (!hospSign.equals(signKeyMD5)) {
            return Result.build(null, ResultCodeEnum.SIGN_ERROR);
        }

        scheduleService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation("查询科室接口")
    @PostMapping("/api/hosp/schedule/list")
    public Result<?> findSchedule(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hospSign = (String) paramMap.get("sign");
        String hosCode = (String) paramMap.get("hoscode");

        Optional<HospitalSet> hospitalSetByHoscode = hospitalSetService.getHospitalSetByHoscode(hosCode);

        String signKeyMD5 = hospitalSetByHoscode
                .map(HospitalSet::getSignKey)
                .map(MD5::encrypt)
                .orElseGet(() -> null);

        if (!hospSign.equals(signKeyMD5)) {
            return Result.build(null, ResultCodeEnum.SIGN_ERROR);
        }


        Integer page = paramMap.get("page") == null ? 1 : Integer.parseInt((String) paramMap.get("page"));
        Integer limit = paramMap.get("limit") == null ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hosCode);
        scheduleQueryVo.setDepcode((String) paramMap.get("depcode"));

        return Result.ok(scheduleService.findPageSchedule(page, limit, scheduleQueryVo));
    }

    @PostMapping("/api/hosp/schedule/remove")
    public Result<?> removeSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hospSign = (String) paramMap.get("sign");
        String hosCode = (String) paramMap.get("hoscode");

        Optional<HospitalSet> hospitalSetByHoscode = hospitalSetService.getHospitalSetByHoscode(hosCode);

        String signKeyMD5 = hospitalSetByHoscode
                .map(HospitalSet::getSignKey)
                .map(MD5::encrypt)
                .orElseGet(() -> null);

        if (!hospSign.equals(signKeyMD5)) {
            return Result.build(null, ResultCodeEnum.SIGN_ERROR);
        }

        String hosScheduleId = (String) paramMap.get("hosScheduleId");

        scheduleService.remove(hosCode, hosScheduleId);
        return Result.ok();
    }

}
