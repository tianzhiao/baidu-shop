package com.baidu.shop.service;

import java.util.Map;

/**
 * @ClassName GoodsService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/23
 * @Version V1.09999999999999999
 **/

public interface GoodsServiceI {
    Map<String, Object> getGoodsBySpuId(Integer spuId);
}
