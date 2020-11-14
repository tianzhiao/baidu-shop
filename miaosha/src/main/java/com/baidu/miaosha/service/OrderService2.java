package com.baidu.miaosha.service;

/**
 * @ClassName OrderService2
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/28
 * @Version V1.09999999999999999
 **/

public interface OrderService2 {
    int createPessimisticOrder(int sid);

    int createVerifiedOrder(Integer sid, Integer userId, String verifyHash);

}
