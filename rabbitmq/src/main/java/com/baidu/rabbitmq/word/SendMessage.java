package com.baidu.rabbitmq.word;

import com.baidu.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName SendMessage
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/9
 * @Version V1.09999999999999999
 **/

public class SendMessage {

    //序列名称
    public static final String QUEUE_NAME = "baidudu_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = RabbitmqConnectionUtil.getConnection();

        //获取通道
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);



        for (int i = 0; i < 100;i++){
            String message = "task - good study -" + i;
            /*
            param1: 交换机名称
            param2: routingKey
            param3: 一些配置信息
            param4: 发送的消息
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());

            System.out.println(" send '" + message + "' success");
        }

        channel.close();
        connection.close();

    }
}
