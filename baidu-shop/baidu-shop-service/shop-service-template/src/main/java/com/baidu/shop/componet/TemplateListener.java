package com.baidu.shop.componet;

import com.baidu.shop.constant.MqMessageConstant;
import com.baidu.shop.service.GoodsServiceI;
import com.baidu.shop.service.impl.TemptaleServiceImpl;
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
import java.io.IOException;

/**
 * @ClassName TemplateListener
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/12
 * @Version V1.09999999999999999
 **/
@Component
@Slf4j
public class TemplateListener {

    @Resource
    private TemptaleServiceImpl temptaleService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqMessageConstant.SPU_QUEUE_PAGE_SAVE,durable = "true"),
            exchange = @Exchange(value = MqMessageConstant.EXCHANGE,ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {MqMessageConstant.SPU_ROUT_KEY_UPDATE,MqMessageConstant.SPU_ROUT_KEY_SAVE}
    ))
    public void save(Message message, Channel channel) throws IOException {

        log.info("template服务接受到需要保存数据的消息: " + new String(message.getBody()));

        temptaleService.createStaticHTMLTemplate(Integer.valueOf(new String(message.getBody())));
        //根据spuId生成页面
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqMessageConstant.SPU_QUEUE_PAGE_DELETE,durable = "true"),
            exchange = @Exchange(value = MqMessageConstant.EXCHANGE,ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = MqMessageConstant.SPU_ROUT_KEY_DELETE
    ))
    public void delete(Message message, Channel channel) throws IOException {

        log.info("template服务接受到需要删除数据的消息: " + new String(message.getBody()));
        //根据spuId生成页面
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }

}
