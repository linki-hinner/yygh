package com.lin.yygh.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.model.user.UserInfo;
import com.lin.yygh.user.mapper.provider.UserInfoMapperProvider;
import com.lin.yygh.vo.user.UserInfoQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @Select({"select * from user_info where phone=#{phone} or openid=#{openid}"})
    UserInfo getUserInfoByPhoneOrOpenid(@Param("phone")String phone,
                                        @Param("openid")String openid);

    @SelectProvider(type = UserInfoMapperProvider.class, method = "ListByUserInfoQueryVo")
    List<UserInfo> ListByUserInfoQueryVo(@Param("userInfoQueryVo")UserInfoQueryVo userInfoQueryVo);
}
