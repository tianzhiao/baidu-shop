package com.baidu.miaosha.service;

import com.baidu.miaosha.entity.Stock;

/**
 * @ClassName StockService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/28
 * @Version V1.09999999999999999
 **/

public interface StockService {
    Stock getStockById(Integer sid);

    int getStockCountByDB(int sid);

}
