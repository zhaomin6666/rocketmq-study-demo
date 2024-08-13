package com.zm.comsumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

public class SqlFilterConsumer {

    public static void main(String[] args) {
        // 构建⼀个消息消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("basic_consumer_group_name");
        // 指定nameserver地址
        consumer.setNamesrvAddr("192.168.30.3:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        try {
            // 订阅⼀个消息队列，使用sql过滤器过滤
            consumer.subscribe("basic_topic", MessageSelector.bySql(
                    "(TAGS in ('basic_tag_A', 'basic_tag_B') AND (ATTRIBUTE is not null and ATTRIBUTE between 0 and 3))"));
            // 注册消息监听器
            consumer.registerMessageListener((MessageListenerConcurrently) (messageExtList, consumeConcurrentlyContext) -> {
                messageExtList.forEach(messageExt -> {
                    System.out.println(new String(messageExt.getBody()));
                });
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            // 启动消费者
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
