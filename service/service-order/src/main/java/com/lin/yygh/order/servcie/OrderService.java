package com.lin.yygh.order.servcie;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.yygh.model.order.OrderInfo;
import com.lin.yygh.vo.order.OrderCountQueryVo;
import com.lin.yygh.vo.order.OrderQueryVo;

import java.util.Map;

public interface OrderService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId, Long patientId);

    IPage<OrderInfo> selectPage(OrderQueryVo orderQueryVo);

    OrderInfo getOrder(String orderId);

    Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);
}
