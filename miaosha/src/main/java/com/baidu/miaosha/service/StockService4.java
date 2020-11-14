package com.baidu.miaosha.service;

/**
 * @ClassName StockService4
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/

public interface StockService4 {
    int getStockCountByDB(int sid);

    Integer getStockCountByCache(int sid);


    void setStockCountToCache(int sid, Integer count);
}
