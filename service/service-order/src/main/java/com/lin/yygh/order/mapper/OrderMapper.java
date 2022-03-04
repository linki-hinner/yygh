package com.lin.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.yygh.model.order.OrderInfo;
import com.lin.yygh.order.mapper.provider.OrderMapperProvider;
import com.lin.yygh.vo.order.OrderCountQueryVo;
import com.lin.yygh.vo.order.OrderCountVo;
import com.lin.yygh.vo.order.OrderQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<OrderInfo> {
    @SelectProvider(type = OrderMapperProvider.class, method = "getPageByOrderQueryVo")
    List<OrderInfo> getPageByOrderQueryVo(@Param("queryVo")OrderQueryVo queryVo);

    @SelectProvider(type = OrderMapperProvider.class, method = "getCountByOrderQueryVo")
    int getCountByOrderQueryVo(@Param("queryVo")OrderQueryVo queryVo);

    @SelectProvider(type = OrderMapperProvider.class, method = "ListOrderCountVo")
    List<OrderCountVo> ListOrderCountVo(@Param("vo") OrderCountQueryVo orderCountQueryVo);
}
