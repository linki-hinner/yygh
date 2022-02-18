package com.lin.yygh.common.utils;

import com.lin.yygh.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

public class AuthContextHolder {
    public static Long getUserId(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        return JwtHelper.getUserId(token);
    }
    //获取当前用户名称
    public static String getUserName(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        return JwtHelper.getUserName(token);
    }
}
