package com.baidu.rabbitmq.dIrect;

import com.baidu.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName ReceiveOut
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/9
 * @Version V1.09999999999999999
 **/

public class ReceiveOut {

    //交换机名字
    public static final String EXCHANGE_NAME = "wang_hao";

    //队列名字
    public static final String QUEUE_NAME = "wang_haohao1";

    public static void main(String[] args) throws IOException, TimeoutException {

        // 获取连接
        Connection connection = RabbitmqConnectionUtil.getConnection();
        // 创建通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //消息队列绑定到交换机
        /*
        param1: 序列名
        param2: 交换机名
        param3: routingKey
         */
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "save");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "update");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "delete");
        // 定义队列 接收端==》消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 监听队列中的消息，如果有消息，进行处理
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                // body： 消息中参数信息
                String msg = new String(body);
                System.out.println(" [消费者1模拟es服务] 接收到消息 : " + msg );
            }
        };
       /*
       param1 : 队列名称
       param2 : 是否自动确认消息
       param3 : 消费者
        */
        channel.basicConsume(QUEUE_NAME, true, consumer);

        //消费者需要时时监听消息，不用关闭通道与连接

    }

}
