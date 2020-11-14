package com.baidu.miaosha.service.impl;

import com.baidu.miaosha.service.StockService5;
import com.baidu.miaosha.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName StockServiceImpl5
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/
@Service
@Slf4j
public class StockServiceImpl5 implements StockService5 {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void delStockCountCache(int id) {
        String hashKey = CacheKey.STOCK_COUNT + "_" + id;
        stringRedisTemplate.delete(hashKey);
        log.debug("删除商品id：[{}] 缓存", id);
    }
}
