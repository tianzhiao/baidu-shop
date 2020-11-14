package com.baidu.miaosha.controller;

import com.baidu.miaosha.service.OrderService;
import com.baidu.miaosha.service.StockService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName OrderController
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/28
 * @Version V1.09999999999999999
 **/
@RestController
@Slf4j
public class OrderController {

   // @Autowired
    private StockService stockService;

    @Autowired
    private OrderService orderService;

    //初始化令牌桶，每秒放行10个请求
    RateLimiter rateLimiter = RateLimiter.create(10);

    @RequestMapping("/createWrongOrders/{sid}")
    @ResponseBody
    public String createWrongOrder(@PathVariable int sid) {
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
     * 乐观锁更新库存 + 令牌桶限流
     * @param sid
     * @return
     */
    @RequestMapping("/createOptimisticOrders/{sid}")
    @ResponseBody
    public String createOptimisticOrder(@PathVariable int sid) {
        // 阻塞式获取令牌
        //LOGGER.info("等待时间" + rateLimiter.acquire());
        // 非阻塞式获取令牌
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            log.warn("你被限流了，真不幸，直接返回失败");
            return "购买失败，库存不足";
        }
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


    /**
     * 事务for update更新库存
     * @param sid
     * @return
     */
    @RequestMapping("/createPessimisticOrder/{sid}")
    @ResponseBody
    public String createPessimisticOrder(@PathVariable int sid) {

        int id;

        try {
            id = orderService.createPessimisticOrder(sid);
            log.info("购买成功，剩余库存为: [{}]", id);
        } catch (Exception e) {
            log.error("购买失败：[{}]", e.getMessage());
            return "购买失败，库存不足";
        }
        return String.format("购买成功，剩余库存为：%d", id);
    }

}
