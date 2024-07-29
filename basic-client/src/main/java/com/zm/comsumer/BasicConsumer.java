package com.zm.comsumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

public class BasicConsumer {

    public static void main(String[] args) {
        // 构建⼀个消息消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("basic_consumer_group_name");
        // 指定nameserver地址
        consumer.setNamesrvAddr("192.168.30.3:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        try {
            // 订阅⼀个消息队列
            consumer.subscribe("basic_topic", "*");
            // 注册消息监听器
            consumer.registerMessageListener((MessageListenerConcurrently) (messageExtList, consumeConcurrentlyContext) -> {
                messageExtList.forEach(messageExt -> {
                    System.out.println(new String(messageExt.getBody()));
                });
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            // 启动消费者
            consumer.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
