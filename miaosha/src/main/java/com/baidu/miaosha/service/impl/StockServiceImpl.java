package com.baidu.miaosha.service.impl;

import com.baidu.miaosha.entity.Stock;
import com.baidu.miaosha.mapper.StockMapper;
import com.baidu.miaosha.service.StockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName StorckServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/
@Service
public class StockServiceImpl implements StockService
{

    @Resource
    private StockMapper stockMapper;

    @Override
    public Stock getStockById(Integer sid) {
        return stockMapper.selectByPrimaryKey(sid);
    }

    @Override
    public int getStockCountByDB(int sid) {
        return 0;
    }
}
