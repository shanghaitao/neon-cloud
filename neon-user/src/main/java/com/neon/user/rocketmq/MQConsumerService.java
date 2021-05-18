package com.neon.user.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;


@Component
@RocketMQMessageListener(topic = "${rocketmq.topic}", consumerGroup = "${rocketmq.producer.group}")
public class MQConsumerService implements RocketMQListener<MessageExt> {

	@Override
	public void onMessage(MessageExt messageExt) {
		System.err.println(new String(messageExt.getBody()));
	}
}
