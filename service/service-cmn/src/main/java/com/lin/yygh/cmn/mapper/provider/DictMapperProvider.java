package com.lin.yygh.cmn.mapper.provider;

import com.lin.yygh.model.base.BaseEntity;

import java.util.Map;

public class DictMapperProvider {

    public final static String SELECT_BASE = " select id, parent_id, name, value, dict_code, has_child "
            + " from dict "
            + " where is_deleted = " + BaseEntity.STATE_SURVIVE;

    public String getByDictCode(Map<String, Object> params){
        StringBuilder sql;
        sql = new StringBuilder();
        sql.append(SELECT_BASE);
        sql.append(" and dict_code = #{dictCode} ");
        return sql.toString();
    }

    public String getByParentId(Map<String, Object> params) {
        Long parentId = (Long) params.get("parentId");
        StringBuilder sql;
        sql = new StringBuilder();
        sql.append(SELECT_BASE);
        sql.append(" and parent_id = #{parentId} ");
        return sql.toString();
    }

}
