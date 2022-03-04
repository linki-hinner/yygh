package com.lin.yygh.cmn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.cmn.mapper.provider.DictMapperProvider;
import com.lin.yygh.model.cmn.Dict;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface DictMapper extends BaseMapper<Dict> {
    @SelectProvider(type = DictMapperProvider.class, method = "getByDictCode")
    Dict getByDictCode(@Param("dictCode") String dictCode);

    @SelectProvider(type = DictMapperProvider.class, method = "getByParentId")
    List<Dict> getByParentId(@Param("parentId") Long dictCode);
}
