package com.lin.yygh.msm.receiver;

import com.lin.yygh.msm.service.MsmService;
import com.lin.yygh.rabbit.constant.MqConst;
import com.lin.yygh.vo.msm.MsmVo;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmsReceiver {
    @Autowired
    private MsmService msmService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_MSM_ITEM, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT),
            key = {MqConst.ROUTING_MSM_ITEM}
    ), containerFactory = "per2ContainerFactory")
    public void send(MsmVo msmVo) {
        msmService.send(msmVo);
    }
}


