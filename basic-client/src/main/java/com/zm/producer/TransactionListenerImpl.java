package com.zm.producer;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zm
 * @version 1.0
 * @date 2024-09-08
 */
public class TransactionListenerImpl implements TransactionListener {
    private AtomicInteger transactionIndex = new AtomicInteger(0);

    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    /**
     * 提交半事务消息后执行本地逻辑
     * @param message 消息实体
     * @param o org.apache.rocketmq.client.producer.TransactionMQProducer#sendMessageInTransaction(org.apache
     *          .rocketmq.common.message.Message, java.lang.Object)中传入的自定义参数
     * @return 事务状态
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        int value = transactionIndex.getAndIncrement();
        int status = value % 3;
        localTrans.put(message.getTransactionId(), status);
        return LocalTransactionState.UNKNOW;
    }

    /**
     * 服务端发起回查时客户端执行的代码
     * @param messageExt 回查的消息
     * @return 事务状态
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        Integer status = localTrans.get(messageExt.getTransactionId());
        if (null != status) {
            return switch (status) {
                case 0 -> LocalTransactionState.UNKNOW;
                case 1 -> LocalTransactionState.COMMIT_MESSAGE;
                case 2 -> LocalTransactionState.ROLLBACK_MESSAGE;
                default -> LocalTransactionState.COMMIT_MESSAGE;
            };
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
