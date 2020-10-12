package com.baidu.rabbitmq.simple;

import com.baidu.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Receive
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/9
 * @Version V1.09999999999999999
 **/

public class Receive {

    public static final String QUEUE_NAME = "goods_zhuangbi";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = RabbitmqConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String s = new String(body,"UTF-8");

                System.out.println(s);

//                int age = 1/0;
  /*
                param1 : （唯一标识 ID）
                param2 : 是否进行批处理
                 */
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

           /*
       param1 : 队列名称
       param2 : 是否自动确认消息
       param3 : 消费者
        */
        channel.basicConsume(QUEUE_NAME, false, defaultConsumer);

        //消费者需要时时监听消息，不用关闭通道与连接
    }

}
