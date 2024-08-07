package com.zm.comsumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

public class ConsumeOrderlyConsumer {

    public static void main(String[] args) {
        // 构建⼀个消息消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("basic_consumer_group_name");
        // 指定nameserver地址
        consumer.setNamesrvAddr("192.168.30.3:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        try {
            // 订阅⼀个消息队列
            consumer.subscribe("consume_orderly_topic", "*");
            // 注册消息监听器，这里监听器使用MessageListenerOrderly
            consumer.registerMessageListener((MessageListenerOrderly) (messageExtList, consumeOrderlyContext) -> {
                consumeOrderlyContext.setAutoCommit(true);
                messageExtList.forEach(messageExt -> {
                    // 可以看到每个queue有唯一的consume线程来消费, 订单对每个queue(分区)有序
                    System.out.println("consumeThread=" + Thread.currentThread().getName() + "queueId=" + messageExt.getQueueId() + ", content:" + new String(messageExt.getBody()));
                });
                return ConsumeOrderlyStatus.SUCCESS;
            });
            // 启动消费者
            consumer.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
