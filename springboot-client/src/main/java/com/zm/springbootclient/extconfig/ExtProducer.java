package com.zm.springbootclient.extconfig;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zm
 * @version 1.0
 * @date 2024-09-09
 */
@Component
public class ExtProducer {
    @Resource(name = "extRocketMQTemplate")
    private RocketMQTemplate extRocketMQTemplate;

    public void sendMessage(String topic, String msg) {
        this.extRocketMQTemplate.convertAndSend(topic, msg);
    }
}
