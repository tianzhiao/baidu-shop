package com.baidu.shop.service.listener;

import com.baidu.shop.constant.MqMessageConstant;
import com.baidu.shop.service.impl.ShopElasticsearchServiceImpl;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.lang.model.element.VariableElement;
import java.io.IOException;

/**
 * @ClassName GoodsListener
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/12
 * @Version V1.09999999999999999
 **/
@Component
@Slf4j
public class GoodsListener {

    @Resource
    private ShopElasticsearchServiceImpl shopElasticsearchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    value = MqMessageConstant.SPU_QUEUE_SEARCH_SAVE,
                    ignoreDeclarationExceptions = "true",
                    durable = "true"
            ),
            exchange = @Exchange(
                    value = MqMessageConstant.EXCHANGE,
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {MqMessageConstant.SPU_ROUT_KEY_SAVE,MqMessageConstant.SPU_ROUT_KEY_UPDATE}
    ))
    public void save(Message message, Channel channel) throws IOException {



        int i = Integer.parseInt(new String(message.getBody()));

        shopElasticsearchService.getGoodsSearchSave(i);
        System.out.println("保存es数据 ZHUJIANWEI : --> " + i);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);

    }

    @RabbitListener(
            bindings = @QueueBinding(

                    value = @Queue(
                            value = MqMessageConstant.SPU_QUEUE_SEARCH_DELETE,
                            ignoreDeclarationExceptions = "true",
                            durable = "true"
                    ), exchange = @Exchange(
                            value = MqMessageConstant.EXCHANGE,
                            ignoreDeclarationExceptions = "true"
                    ),
                        key = MqMessageConstant.SPU_ROUT_KEY_DELETE
            )
    )
    public void delete(Message message,Channel channel) throws IOException {


        log.info("保存es数据 ID：" + message.getBody().toString());

        int i = Integer.parseInt(new String(message.getBody()));
        System.out.println("保存es数据 ZHUJIANWEI : --> " + i);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);

    }
}

