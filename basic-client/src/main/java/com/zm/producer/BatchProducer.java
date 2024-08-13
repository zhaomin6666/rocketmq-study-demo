package com.zm.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.ArrayList;
import java.util.List;

public class BatchProducer {
    public static void main(String[] args) {
        // 初始化一个消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("batch_producer_group_name");
        producer.setNamesrvAddr("192.168.30.3:9876");
        try {
            // 启动生产者
            producer.start();
            // 创建消息对象
            List<Message> msgList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Message msg = new Message("batch_topic", "batch_tag_A",
                        ("Hello RocketMQ" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                msgList.add(msg);
            }
            // 发送消息
            SendResult sendResult = producer.send(msgList);
            System.out.printf("%s%n", sendResult);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            producer.shutdown();
        }
    }
}
