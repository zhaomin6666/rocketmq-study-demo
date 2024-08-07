package com.zm.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

public class ConsumeOrderlyProducer {
    public static void main(String[] args) {
        // 初始化一个消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("consume_orderly_producer_group_name");
        producer.setNamesrvAddr("192.168.30.3:9876");
        try {
            // 启动生产者
            producer.start();
            for (int businessId = 0; businessId < 10; businessId++) {
                // 模拟10次业务过程
                for (int j = 0; j < 5; j++) {
                    // 模拟1次业务的5个流程
                    String progressId = businessId + "_" + j;
                    // 创建消息对象
                    Message msg = new Message("consume_orderly_topic", "tag_business_" + businessId, "key_" + progressId,
                            ("Hello RocketMQ " + progressId).getBytes(RemotingHelper.DEFAULT_CHARSET));
                    // 发送消息
                    SendResult sendResult = producer.send(msg, (mqs, msg1, arg) -> {
                        // 这里的arg就是send方法的第三个参数businessId
                        Integer id = (Integer) arg;
                        int index = id % mqs.size();
                        return mqs.get(index);
                    }, businessId);
                    System.out.printf("%s%n", sendResult);
                }
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            producer.shutdown();
        }
    }
}
