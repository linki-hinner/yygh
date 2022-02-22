package com.lin.yygh.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.model.user.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @Select({"select * from user_info where phone=#{phone} or openid=#{openid}"})
    UserInfo getUserInfoByPhoneOrOpenid(@Param("phone")String phone,
                                        @Param("openid")String openid);
}
