package com.lin.yygh.user.api;

import com.lin.yygh.common.result.Result;
import com.lin.yygh.common.utils.AuthContextHolder;
import com.lin.yygh.model.user.UserInfo;
import com.lin.yygh.user.service.UserInfoService;
import com.lin.yygh.vo.user.LoginVo;
import com.lin.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserInfoApiController {
    private  UserInfoService userInfoService;

    @ApiOperation(value = "会员登录")
    @PostMapping("/api/user/login")
    public Result<Map<String, ?>> login(@RequestBody LoginVo loginVo) {
        Map<String, Object> info = userInfoService.login(loginVo);
        return Result.ok(info);
    }

    //用户认证接口
    @PostMapping("/api/user/auth/userAuth")
    public Result<?> userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        //传递两个参数，第一个参数用户id，第二个参数认证数据vo对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();
    }

    //获取用户id信息接口
    @GetMapping("/api/user/auth/getUserInfo")
    public Result<?> getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        if(userInfo != null){
            userInfo.getParam().put("certificatesTypeString", "10".equals(userInfo.getCertificatesType()) ? "身份证" : "户口本");
        }
        return Result.ok(userInfo);
    }

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }
}
