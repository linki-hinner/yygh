package com.lin.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.model.order.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderInfo> {
}
