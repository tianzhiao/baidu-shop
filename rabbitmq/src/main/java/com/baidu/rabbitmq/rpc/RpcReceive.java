package com.baidu.rabbitmq.rpc;

import com.baidu.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.*;

import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName RpcReceive
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/10
 * @Version V1.09999999999999999
 **/

public class RpcReceive {


    //交换机
    public static final String EXCHANGE_NAME = "exchange_rpc";
    //队列名字
    public static final String QUEUE_NAME = "queue_rpc";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = RabbitmqConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);

                    System.out.println("收到消息 执行中：" + new String(body));

                    AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
                    //我们在将要回复的消息属性中，放入从客户端传递过来的correlateId 关系id
                    builder.correlationId(properties.getCorrelationId());
                    AMQP.BasicProperties prop = builder.build();

                    //发送给回复队列的消息，exchange=""，routingKey=回复队列名称
                    //因为RabbitMQ对于队列，始终存在一个默认exchange=""，routingKey=队列名称的绑定关系
                    String message = new String(body) + "-收到 over 一定 study";
                    channel.basicPublish("", properties.getReplyTo(), prop, message.getBytes());
                }
        };

        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);

    }
}