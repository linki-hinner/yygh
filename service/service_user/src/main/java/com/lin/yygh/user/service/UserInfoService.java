package com.lin.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.yygh.model.user.UserInfo;
import com.lin.yygh.vo.user.LoginVo;
import com.lin.yygh.vo.user.UserAuthVo;
import com.lin.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> login(LoginVo loginVo);

    void userAuth(Long userId, UserAuthVo userAuthVo);

    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    Map<String, Object> show(Long userId);

    void lock(Long userId, Integer status);

    void approval(Long userId, Integer authStatus);
}
