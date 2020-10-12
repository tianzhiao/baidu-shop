package com.baidu.rabbitmq.word;

import com.baidu.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName RecevieTwo
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/9
 * @Version V1.09999999999999999
 **/

public class ReceiveTwo {
    //序列名称
    public static final String QUEUE_NAME = "baidudu_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        //获取连接
        Connection connection = RabbitmqConnectionUtil.getConnection();

        //创建通道
        Channel channel = connection.createChannel();


        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //执行完成之后 立即执行下一个
        channel.basicQos(1);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String s = new String(body);
                System.out.println(" [消费者-2] 收到消息 : " + s);


                try {
                    /**
                     * 多少时间拆能访问
                     */
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        channel.basicConsume(QUEUE_NAME,false,defaultConsumer);
    }
}
