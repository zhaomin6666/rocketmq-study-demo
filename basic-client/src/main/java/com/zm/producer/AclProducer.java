package com.zm.producer;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class AclProducer {

    public static void main(String[] args) {
        // 声明身份信息
        String ACL_ACCESS_KEY = "RocketMQ";
        String  ACL_SECRET_KEY= "12345678";
        RPCHook rpcHook = new AclClientRPCHook(new SessionCredentials(ACL_ACCESS_KEY,ACL_SECRET_KEY));
        // 初始化一个消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("acl_producer_group_name", rpcHook);
        producer.setNamesrvAddr("192.168.30.3:9876");
        try {
            // 启动生产者
            producer.start();
            // 创建消息对象
            Message msg = new Message("acl_topic", "acl_tag_A",
                    "Hello RocketMQ".getBytes(RemotingHelper.DEFAULT_CHARSET));
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
