package com.baidu.rabbitmq.topic;

import com.baidu.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName SandMessage
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/9
 * @Version V1.09999999999999999
 **/

public class SandMessage {

    public static final String  EXCHANGE_NAME = "topic_exchange_test";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = RabbitmqConnectionUtil.getConnection();

        Channel channel = connection.createChannel();
        //发送的消息内容
        String message = "商品删除成功  id ： 153";
        /*
        param1: 交换机名称
        param2: routingKey
        param3: 一些配置信息
        param4: 发送的消息
        MessageProperties.PERSISTENT_TEXT_PLAIN 消息持久化
         */
        //发送消息
        channel.basicPublish(EXCHANGE_NAME, "goods.save.liuliu", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        //channel.basicPublish(EXCHANGE_NAME, "update", null, message.getBytes());
        //channel.basicPublish(EXCHANGE_NAME, "delete", null, message.getBytes());

        System.out.println(" [商品服务] 发送消息routingKey ：save   '" + message );
        // 关闭通道和连接
        channel.close();
        connection.close();

    }

}
