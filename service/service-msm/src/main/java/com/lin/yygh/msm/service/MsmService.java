package com.lin.yygh.msm.service;

import com.lin.yygh.vo.msm.MsmVo;

public interface MsmService {

    //发送手机验证码
    boolean send(String phone, String code);

    boolean send(MsmVo msmVo);
}
