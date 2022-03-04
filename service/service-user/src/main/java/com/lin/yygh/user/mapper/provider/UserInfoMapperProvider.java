package com.lin.yygh.user.mapper.provider;

import com.lin.yygh.model.base.BaseEntity;
import com.lin.yygh.vo.user.UserInfoQueryVo;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class UserInfoMapperProvider {
    public static final String BASE_SELECT = "select id, openid, nick_name, phone, name, certificates_type, " +
            " certificates_no, certificates_url, auth_status, status " +
            " from user_info " +
            " where is_deleted = " + BaseEntity.STATE_SURVIVE;

    public String ListByUserInfoQueryVo(Map<String, Object> params) {
        UserInfoQueryVo userInfoQueryVo = (UserInfoQueryVo) params.get("userInfoQueryVo");
        String name = userInfoQueryVo.getKeyword(); //用户名称
        Integer status = userInfoQueryVo.getStatus();//用户状态
        Integer authStatus = userInfoQueryVo.getAuthStatus(); //认证状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin(); //开始时间
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd(); //结束时间

        StringBuilder sql = new StringBuilder(BASE_SELECT);
        if(!StringUtils.isEmpty(name)) {
            sql.append(" and name like %").append(name).append("%");
        }
        if(status != null) {
            sql.append("status = ").append(status);
        }
        if(authStatus != null) {
            sql.append("auth_status = ").append(status);
        }
        if(!StringUtils.isEmpty(createTimeBegin)) {
            sql.append("create_time >= ").append(createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)) {
            sql.append("create_time <= ").append(createTimeEnd);
        }
        return sql.toString();
    }
}
