package com.baidu.rabbitmq.dIrect;

import com.baidu.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

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

    //交换机名字
    public static final String EXCHANGE_NAME = "wang_hao";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = RabbitmqConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"direct");

//        String message = "新增成功哦" + 199;
        String message = "删除成功哦" + 199;
//        String message = "修改成功哦" + 199;
//        channel.basicPublish(EXCHANGE_NAME,"save",null,message.getBytes());
//        channel.basicPublish(EXCHANGE_NAME,"update",null,message.getBytes());
        channel.basicPublish(EXCHANGE_NAME,"delete",null,message.getBytes());

        System.out.println(" [商品服务] 发送消息routingKey ：save   '" + message );
        // 关闭通道和连接
        channel.close();
        connection.close();
    }
}
