package com.baidu.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName RabbitmqConnectionUtil
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/9
 * @Version V1.09999999999999999
 **/

public class RabbitmqConnectionUtil {

    public static Connection getConnection() throws IOException, TimeoutException {

        //定义rabbit mq连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置超时时间
        connectionFactory.setConnectionTimeout(60000);

        //设置服务
        connectionFactory.setHost("127.0.0.1");
        //
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = connectionFactory.newConnection();
        return connection;

    }
}
