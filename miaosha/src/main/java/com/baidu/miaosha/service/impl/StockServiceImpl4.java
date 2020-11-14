package com.baidu.miaosha.service.impl;

import com.baidu.miaosha.entity.Stock;
import com.baidu.miaosha.mapper.StockMapper;
import com.baidu.miaosha.service.StockService4;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName StockServiceImpl4
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/
@Service
public class StockServiceImpl4 implements StockService4 {

    @Resource
    private StockMapper stockMapper;

    @Override
    public int getStockCountByDB(int id) {
        Stock stock = stockMapper.selectByPrimaryKey(id);
        return stock.getCount() - stock.getSale();
    }

    @Override
    public Integer getStockCountByCache(int sid) {

        return null;
    }

    @Override
    public void setStockCountToCache(int sid, Integer count) {

    }
}
