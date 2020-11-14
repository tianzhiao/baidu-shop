package com.baidu.miaosha.service;

/**
 * @ClassName OrderService3
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/

public interface OrderService3 {

    int createVerifiedOrder(Integer sid, Integer userId, String verifyHash);
}
