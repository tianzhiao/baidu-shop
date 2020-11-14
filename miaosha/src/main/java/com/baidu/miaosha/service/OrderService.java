package com.baidu.miaosha.service;

/**
 * @ClassName OrderService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/27
 * @Version V1.09999999999999999
 **/

public interface OrderService {


    int createWrongOrder(int sid);

    int createOptimisticOrder(int sid);


    int createPessimisticOrder(int sid);
}
