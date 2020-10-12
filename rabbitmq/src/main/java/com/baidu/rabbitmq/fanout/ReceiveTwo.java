package com.baidu.rabbitmq.fanout;

import com.baidu.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName ReceiveTwo
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/9
 * @Version V1.09999999999999999
 **/

public class ReceiveTwo {


    //交换机名称
    public static final String EXCHANGE_NAME = "cui_ge";

    //队列名字
    public static final String QUEUE_NAME = "cui_di";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitmqConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);

                String s = new String(body);

                System.out.println(" [消费者-2] 收到消息 : " + s);
            }
        };


        channel.basicConsume(QUEUE_NAME,true,defaultConsumer);

    }

}
