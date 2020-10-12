package com.baidu.rabbitmq.fanout;

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


    //交换机名称
    public static final String EXCHANGE_NAME = "cui_ge";

    //队列名字
    public static final String QUEUE_NAME = "cui_di";

    public static void main(String[] args) throws IOException, TimeoutException {

        //获取连接
        Connection connection = RabbitmqConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        /**
         * param1 队列名字
         * param2 交换机名字
         * param3 routingkey
         */
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String message = new String(body);
                System.out.println(" [消费者-1] 收到消息 : " + message );

            }
        };

          /*
       param1 : 队列名称
       param2 : 是否自动确认消息
       param3 : 消费者
        */
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);

        //消费者需要时时监听消息，不用关闭通道与连接
    }
}
