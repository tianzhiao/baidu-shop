package com.baidu.shop.business;

import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.dto.UserDTO;
import com.baidu.shop.entity.UserEntity;
import com.baidu.shop.redis.RedisRepository;

/**
 * @ClassName UserService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/15
 * @Version V1.09999999999999999
 **/

public interface UserService {
    String login(UserDTO userDTO, JwtConfig jwtConfig, RedisRepository redisRepository);


    UserEntity getUserByPhone(String phone);
}
