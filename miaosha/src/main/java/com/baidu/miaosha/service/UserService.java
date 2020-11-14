package com.baidu.miaosha.service;

/**
 * @ClassName UserService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/

public interface UserService {
    String getVerifyHash(Integer sid, Integer userId);

    int addUserCount(Integer userId);

    boolean getUserIsBanned(Integer userId);
}
