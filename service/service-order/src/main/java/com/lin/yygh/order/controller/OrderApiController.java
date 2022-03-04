package com.lin.yygh.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.yygh.common.result.Result;
import com.lin.yygh.common.utils.AuthContextHolder;
import com.lin.yygh.enums.OrderStatusEnum;
import com.lin.yygh.model.order.OrderInfo;
import com.lin.yygh.order.servcie.OrderService;
import com.lin.yygh.vo.order.OrderCountQueryVo;
import com.lin.yygh.vo.order.OrderQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "订单接口")
@RestController
//@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "创建订单")
    @PostMapping("/api/order/orderInfo/auth/submitOrder/{scheduleId}/{patientId}")
    public Result submitOrder(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable String scheduleId,
            @ApiParam(name = "patientId", value = "就诊人id", required = true)
            @PathVariable Long patientId) {
        return Result.ok(orderService.saveOrder(scheduleId, patientId));
    }

    @ApiOperation(value = "订单列表（条件查询带分页)")
    @GetMapping("/api/order/orderInfo/auth/page")
    public Result<?> list(OrderQueryVo orderQueryVo, HttpServletRequest request) {
        //设置当前用户id
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        IPage<OrderInfo> pageModel =
                orderService.selectPage(orderQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取订单状态")
    @GetMapping("/api/order/orderInfo/auth/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @ApiOperation(value = "获取订单")
    @GetMapping("/api/order/orderInfo/auth/getOrderInfo/{orderId}")
    public Result getOrders(@PathVariable String orderId) {
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return Result.ok(orderInfo);
    }


    @ApiOperation(value = "获取订单统计数据")
    @PostMapping("/api/order/orderInfo/auth/inner/getCountMap")
    public Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo) {
        return orderService.getCountMap(orderCountQueryVo);
    }
}
