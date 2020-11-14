package com.baidu.shop.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SkillRabbitMQConfig
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/27
 * @Version V1.09999999999999999
 **/

@Configuration
public class SkillRabbitMQConfig {

//    @Value("${order.exchange}")
    private String skillExchange = "order_exchange";

//    @Value("${order.queue}")
    private String skillQueue = "order_queue";

//    @Value("${order.routingKey}")
    private String skillRoutingKey = "order_routingKey";

//    @Value("${order.timeout.exchange}")
    private String skillTimeoutExchange = "order_timeout_exchange";

//    @Value("${order.timeout.queue}")
    private String skillTimeoutQueue = "order_timeout_queue";

//    @Value("${order.timeout.routingKey}")
    private String skillTimeoutRoutingKey = "order_timeout_routingKey";

//    @Value("${order.dlx.exchange}")
    private String skillDlxExchange = "order_dlx_exchange";

//    @Value("${order.dlx.queue}")
    private String skillDlxQueue = "order_dlx_queue";

//    @Value("${order.dlx.routingKey}")
    private String skillDlxRoutingKey = "order_dlx_routingKey";

    @Bean
    public Queue skillQueue() {
        return new Queue(skillQueue);
    }


    @Bean
    public DirectExchange skillExchange() {
        return new DirectExchange(skillExchange);
    }


    @Bean
    public Binding bindingExchange() {
        return BindingBuilder.bind(skillQueue()).to(skillExchange()).with(skillRoutingKey);
    }

    @Bean
    public Queue skillTimeoutQueue() {
        Map<String, Object> arguments = new HashMap<>(2);
        // 绑定我们的死信交换机
        arguments.put("x-dead-letter-exchange", skillDlxExchange);
        // 绑定我们的路由key
        arguments.put("x-dead-letter-routing-key", skillDlxRoutingKey);
        return new Queue(skillTimeoutQueue, true, false, false, arguments);
    }


    @Bean
    public DirectExchange skillTimeoutExchange() {
        return new DirectExchange(skillTimeoutExchange);
    }


    @Bean
    public Binding bindingTimeoutExchange() {
        return BindingBuilder.bind(skillTimeoutQueue()).to(skillTimeoutExchange()).with(skillTimeoutRoutingKey);
    }

    @Bean
    public Queue skillDlxQueue() {
        return new Queue(skillDlxQueue);
    }


    @Bean
    public DirectExchange skillDlxExchange() {
        return new DirectExchange(skillDlxExchange);
    }


    @Bean
    public Binding bindingDlxExchange() {
        return BindingBuilder.bind(skillDlxQueue()).to(skillDlxExchange()).with(skillDlxRoutingKey);
    }
}