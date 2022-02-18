package com.lin.yygh.order.servcie;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.yygh.model.order.OrderInfo;
import com.lin.yygh.vo.order.OrderQueryVo;

public interface OrderService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId, Long patientId);

    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    OrderInfo getOrder(String orderId);
}
