package com.zm.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 只管往Broker发送消息
 */
public class SendOneWayProducer {
    public static void main(String[] args) {
        // 初始化一个消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("basic_producer_group_name");
        producer.setNamesrvAddr("192.168.30.3:9876");
        try {
            // 启动生产者
            producer.start();
            // 创建消息对象
            Message msg = new Message("basic_topic", "basic_tag_A", "Hello RocketMQ".getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送消息
            producer.sendOneway(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            producer.shutdown();
        }
    }
}
