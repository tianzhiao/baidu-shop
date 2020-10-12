package com.baidu.rabbitmq.simple;

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

    public static final String QUEUE_NAME = "goods_zhuangbi";


    public static void main(String[] args) throws IOException, TimeoutException {

        //
        Connection connection = RabbitmqConnectionUtil.getConnection();


        /*
        param1:队列名称
        param2: 是否持久化
        param3: 是否排外
        param4: 是否自动删除
        param5: 其他参数
         */
        //通道
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message = "啭匕 0.0.0.0.0.0.0.0.0.0..0..0.0.0.0.0.0.0.0***************";

        Integer age = 1;

        age.toString();


           /*
        param1: 交换机名称
        param2: routingKey
        param3: 一些配置信息
        param4: 发送的消息
         */
        //发送消息
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());

        System.out.println(" 消息发送 '" + message + "' 到队列 success");
        channel.close();
        connection.close();
    }

}
