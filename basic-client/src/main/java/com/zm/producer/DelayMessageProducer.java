package com.zm.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class DelayMessageProducer {
    public static void main(String[] args) {
        // 初始化一个消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("basic_producer_group_name");
        producer.setNamesrvAddr("192.168.30.3:9876");
        try {
            // 启动生产者
            producer.start();
            // 创建消息对象
            Message msg = new Message("delay_topic", "delay_tag_A",
                    "Hello RocketMQ".getBytes(RemotingHelper.DEFAULT_CHARSET));
            msg.setDelayTimeLevel(2);
            // 发送消息
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            producer.shutdown();
        }
    }
}
