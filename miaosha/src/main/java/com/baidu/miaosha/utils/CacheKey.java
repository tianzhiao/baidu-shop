package com.baidu.miaosha.utils;

/**
 * @ClassName CacheKey
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/

public enum CacheKey {


    HASH_KEY("miaosha_hash"),
    LIMIT_KEY("miaosha_limit"),
    STOCK_COUNT("miaosha_v1_stock_count");

    private String key;

    private CacheKey(String key) {
        this.key = key;
    }
    public String getKey() {
        return key;
    }
}
