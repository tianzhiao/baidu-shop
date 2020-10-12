package com.baidu.rabbitmq.rpc;

import com.baidu.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName RpcSandMessage
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/10
 * @Version V1.09999999999999999
 **/

public class RpcSandMessage {

    //交换机
    public static final String EXCHANGE_NAME = "exchange_rpc";
    //队列名字
    public static final String QUEUE_NAME = "queue_rpc";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = RabbitmqConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        //创建交换机--先删除后增加
        channel.exchangeDelete(EXCHANGE_NAME);
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", false, false, null);

        //创建队列
        channel.queueDelete(QUEUE_NAME);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //绑定队列交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "rpc");

        //此处注意：我们声明了要回复的队列。队列名称由RabbitMQ自动创建。
        //这样做的好处是：每个客户端有属于自己的唯一回复队列，生命周期同客户端
        String replyQueue = channel.queueDeclare().getQueue();

        final String corrID ="9527";

        //消息 指定回复队列和ID
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        // 指定回复队列和回复correlateId
        builder.replyTo(replyQueue).correlationId(corrID);
        AMQP.BasicProperties properties = builder.build();

        // 消息参数内容
        String message = "good good study ";


        channel.basicPublish(EXCHANGE_NAME, "rpc", properties,
                message.getBytes());

        System.out.println("rpc模型消息发送  "+message);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 监听队列中的消息，如果有消息，进行处理
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                if (corrID.equals(properties.getCorrelationId())) {
                    System.out.println("9527  ID对应上的消息：" + new String(body));
                } else {
                    System.out.println("9527  ID未对应上的消息：" + new String(body));
                }
            }
        };
        channel.basicConsume(replyQueue, true, consumer);

    }
}
