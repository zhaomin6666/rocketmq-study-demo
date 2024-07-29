package com.zm.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.CountDownLatch;

/**
 * 只管往Broker发送消息
 */
public class SendAsyncProducer {
    public static void main(String[] args) {
        // 初始化一个消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("basic_producer_group_name");
        producer.setNamesrvAddr("192.168.30.3:9876");
        producer.setSendMsgTimeout(200);
        try {
            // 启动生产者
            producer.start();
            CountDownLatch countDownLatch = new CountDownLatch(10);
            for (int index = 0; index < 10; index++) {
                // 创建消息对象
                Message msg = new Message("basic_topic", "basic_tag_A", ("Hello RocketMQ " + index).getBytes(RemotingHelper.DEFAULT_CHARSET));
                // 发送消息
                int finalIndex = index;
                producer.send(msg, new SendCallback(){
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d OK %s %n", finalIndex, sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d Exception %s %n", finalIndex, e);
                        e.printStackTrace();
                    }
                });
            }
            if (countDownLatch.await(5, java.util.concurrent.TimeUnit.SECONDS)) {
                producer.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
