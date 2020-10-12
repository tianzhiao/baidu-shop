package com.baidu.rabbitmq.word;

import com.baidu.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName ReceiveOne
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/9
 * @Version V1.09999999999999999
 **/

public class ReceiveOne {

    //序列名称
    public static final String QUEUE_NAME = "baidudu_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = RabbitmqConnectionUtil.getConnection();
        //获取通道
        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //执行完成之后 立即执行下一个
        channel.basicQos(1);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String s = new String(body);
                System.out.println(" [消费者-1] 收到消息 : " + s );
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
   /*s
       param1 : 队列名称
       param2 : 是否自动确认消息
       param3 : 消费者
        */
        channel.basicConsume(QUEUE_NAME,false,defaultConsumer);
        //消费者需要时时监听消息，不用关闭通道与连接
    }


}
