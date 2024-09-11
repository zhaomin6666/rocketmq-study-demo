package com.zm.springbootclient.extconfig;

import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

/**
 * @author zm
 * @version 1.0
 * @date 2024-09-09
 */
@ExtRocketMQTemplateConfiguration(nameServer = "${rocketmqB.name-server}",group = "${rocketmqB.producer.group}")
public class ExtRocketMQTemplate extends RocketMQTemplate {
}
