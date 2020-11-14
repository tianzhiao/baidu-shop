package com.baidu.miaosha.controller;

import com.baidu.miaosha.service.OrderService5;
import com.baidu.miaosha.service.StockService5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @ClassName OrderController5
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/
@Slf4j
@Controller
public class OrderController5 {

    @Resource
    private StockService5 stockService;

    @Resource
    private OrderService5 orderService;

    /**
     * 下单接口：先删除缓存，再更新数据库
     * @param sid
     * @return
     */
    @RequestMapping("/createOrderWithCacheV1/{sid}")
    @ResponseBody
    public String createOrderWithCacheV1(@PathVariable int sid) {
        int count = 0;
        try {
            // 删除库存缓存
            stockService.delStockCountCache(sid);
            // 完成扣库存下单事务
            orderService.createPessimisticOrder(sid);
        } catch (Exception e) {
            log.error("购买失败：[{}]", e.getMessage());
            return "购买失败，库存不足";
        }
        log.debug("购买成功，剩余库存为: [{}]", count);
        return String.format("购买成功，剩余库存为：%d", count);
    }

}
