package com.zm.springbootclient.basic;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zm
 * @version 1.0
 * @date 2024-09-09
 */
@Component
public class BasicProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(String topic, String msg) {
        this.rocketMQTemplate.convertAndSend(topic, msg);
    }
}
