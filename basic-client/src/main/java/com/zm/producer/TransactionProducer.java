package com.zm.producer;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TransactionProducer {
    public static void main(String[] args) {
        // 初始化一个消息生产者
        TransactionMQProducer producer = new TransactionMQProducer("transaction_producer_group_name");
        // 创建事务监听器
        TransactionListener transactionListener = new TransactionListenerImpl();
        // 创建执行监听器中代码的线程池
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), r -> {
            Thread thread = new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        });
        producer.setExecutorService(executorService);

        producer.setNamesrvAddr("192.168.30.3:9876");
        try {
            // 启动生产者
            producer.start();
            for (int i = 0; i < 10; i++) {
                // 创建消息对象
                Message msg = new Message("transaction_topic", "transaction_tag_A", "Hello RocketMQ".getBytes(RemotingHelper.DEFAULT_CHARSET));
                // 发送消息
                SendResult sendResult = producer.send(msg);
                System.out.printf("%s%n", sendResult);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            producer.shutdown();
        }
    }
}
