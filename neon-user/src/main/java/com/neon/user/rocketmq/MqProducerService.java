package com.neon.user.rocketmq;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author shanghaitao
 * @Date 2021/5/14 11:42
 */
@Component
public class MqProducerService {
    @Autowired
    protected RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RocketMQConfig rocketMQConfig;

    public String send() {
        String destination = rocketMQConfig.getDestination();
        System.err.println("destination:"+destination);
        SendResult sendResult = rocketMQTemplate.syncSend(destination, MessageBuilder.withPayload("啦啦啦♪(^∇^*)").build(), 1000,16);
        System.err.println("msgId:"+sendResult.getMsgId());
        return sendResult.getMsgId();
    }
}
