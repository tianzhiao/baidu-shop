package com.baidu.miaosha.controller;

import com.baidu.miaosha.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName DemoController
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/27
 * @Version V1.09999999999999999
 **/
@RestController
@Slf4j
public class DemoController {

    @Resource
    private OrderService orderService;

    @RequestMapping("/createWrongOrder/{sid}")
    public String createWrongOrder(@PathVariable int sid) {
        log.info("购买物品编号sid=[{}]", sid);
        int id = 0;
        try {
            id = orderService.createWrongOrder(sid);
            log.info("创建订单id: [{}]", id);
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return String.valueOf(id);
    }


    /**
     * 乐观锁更新库存
     * @param sid
     * @return
     */
    @RequestMapping("/createOptimisticOrder/{sid}")
    public String createOptimisticOrder(@PathVariable int sid) {
        int id;
        try {
            id = orderService.createOptimisticOrder(sid);
            log.info("购买成功，剩余库存为: [{}]", id);
        } catch (Exception e) {
            log.error("购买失败：[{}]", e.getMessage());
            return "购买失败，库存不足";
        }
        return String.format("购买成功，剩余库存为：%d", id);
    }

}
