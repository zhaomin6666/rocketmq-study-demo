package com.zm.springbootclient.basic;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author zm
 * @version 1.0
 * @date 2024-09-09
 */
@Component
@RocketMQMessageListener(consumerGroup = "BasicGroup", topic =
        "BasicTopic", consumeMode = ConsumeMode.CONCURRENTLY, messageModel =
        MessageModel.BROADCASTING)
public class BasicConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("Received message : " + message);
    }
}
