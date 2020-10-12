package com.baidu.rabbitmq.fanout;

import com.baidu.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.*;

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

    //交换机名称
    public static final String EXCHANGE_NAME = "cui_ge";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {


        Connection connection = RabbitmqConnectionUtil.getConnection();

        Channel channel = connection.createChannel();// 创建通道

        //当前模式处于确认模式
        channel.confirmSelect();

        /*
        param1 交换机名称
        param2 交换机类型
        param3 持久化数据
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        //发送消息内容
        String msg = "努力把 弟弟，不行了";


        /**
         * param1` 交换机名字
         * param2 routingKey
         * param3 一般的配置
         * param4 发送的消息
         */
        //基本发布(发送消息)
        channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());

        if (channel.waitForConfirms()) {

            System.out.println("成功");

        }else {
            System.out.println("失败");
        }

        System.out.println(" 消息发送 '" + msg + "' 到交换机 success");
        // 关闭通道和连接
        channel.close();
        connection.close();

    }
}
