package com.baidu.miaosha.controller;

import com.baidu.miaosha.service.OrderService2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName s
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/28
 * @Version V1.09999999999999999
 **/
@Slf4j
@RestController
public class OrderControllers {

    @Resource
    private OrderService2 service2;

    /**
     * 事务for update更新库存
     * @param sid
     * @return
     */
    @RequestMapping("/createPessimisticOrders/{sid}")
    public String createPessimisticOrder(@PathVariable int sid) {
        int id;
        try {
            id = service2.createPessimisticOrder(sid);
            log.info("购买成功，剩余库存为: [{}]", id);
        } catch (Exception e) {
            log.error("购买失败：[{}]", e.getMessage());
            return "购买失败，库存不足";
        }
        return String.format("购买成功，剩余库存为：%d", id);
    }
}
