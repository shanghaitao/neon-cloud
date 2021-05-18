package com.neon.user.rocketmq;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class RocketMQConfig {

	@Value("${rocketmq.name-server}")
	private String nameServer;
	@Value("${rocketmq.producer.group}")
	private String producerGroup;
	@Value("${rocketmq.topic}")
	private String topic;
	@Value("${rocketmq.tag}")
	private String tag;

	public String getDestination(){
		return this.getTopic() + ":" + this.getTag();
	}
}