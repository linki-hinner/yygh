package com.lin.yygh.rabbit.constant;

public class MqConst {

    public static final String EXCHANGE_DIRECT
            = "exchange.direct";
    /**
     * 预约下单
     */
    public static final String ROUTING_ORDER = "order";
    public static final String QUEUE_ORDER  = "queue.order";

    /**
     * 短信
     */
    public static final String ROUTING_MSM_ITEM = "msm.item";
    public static final String QUEUE_MSM_ITEM  = "queue.msm.item";
}
