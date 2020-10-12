package com.baidu.rabbitmq.springamqp;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName RabbitmqDemo
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/12
 * @Version V1.09999999999999999
 **/
@Component
public class RabbitmqDemo {


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            value = "topic_exchange_queue1",
                            durable = "true"
                    ), exchange = @Exchange(
                            value = "exchange_test",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = {"#.#"}
            )
    )
    public void listen(String msg){
        System.out.println("接收消息 ： " + msg);
    }


}
