package com.baidu.miaosha.service.impl;

import com.baidu.miaosha.entity.Stock;
import com.baidu.miaosha.entity.UserEntity;
import com.baidu.miaosha.mapper.UserMapper;
import com.baidu.miaosha.service.StockService;
import com.baidu.miaosha.service.UserService;
import com.baidu.miaosha.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    private static final String SALT = "randomString";
    private static final int ALLOW_COUNT = 10;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private StockService stockService;


    @Override
    public int addUserCount(Integer userId) {
        String limitKey = CacheKey.LIMIT_KEY.getKey() + "_" + userId;
        String limitNum = stringRedisTemplate.opsForValue().get(limitKey);
        int limit = -1;
        if (limitNum == null) {
            stringRedisTemplate.opsForValue().set(limitKey, "0", 3600, TimeUnit.SECONDS);
        } else {
            limit = Integer.parseInt(limitNum) + 1;
            stringRedisTemplate.opsForValue().set(limitKey, String.valueOf(limit), 3600, TimeUnit.SECONDS);
        }
        return limit;
    }

    @Override
    public boolean getUserIsBanned(Integer userId) {
        String limitKey = CacheKey.LIMIT_KEY.getKey() + "_" + userId;
        String limitNum = stringRedisTemplate.opsForValue().get(limitKey);
        if (limitNum == null) {
            log.error("该用户没有访问申请验证值记录，疑似异常");
            return true;
        }
        return Integer.parseInt(limitNum) > ALLOW_COUNT;
    }





    @Override
    public String getVerifyHash(Integer sid, Integer userId) {

        // 验证是否在抢购时间内
        log.info("请自行验证是否在抢购时间内");

        // 检查用户合法性 查看用是否存在
        UserEntity userEntity = userMapper.selectByPrimaryKey(userId);
        if (userEntity == null) {
            throw new RuntimeException("用户不存在");
        }
        log.info("用户信息：[{}]", userEntity.toString());

        // 检查商品合法性 是否有库存数据
        Stock stock = stockService.getStockById(sid);

        if (stock == null) {
            throw new RuntimeException("商品不存在");
        }

        log.info("商品信息：[{}]", stock.toString());

        // 生成hash
        String verify = SALT + sid + userId;

        //MD5加密
        String verifyHash = DigestUtils.md5DigestAsHex(verify.getBytes());

        // 将hash和用户商品信息存入redis
        String hashKey = CacheKey.HASH_KEY.getKey() + "_" + sid + "_" + userId;

        stringRedisTemplate.opsForValue().set(hashKey, verifyHash, 3600, TimeUnit.SECONDS);

        log.info("Redis写入：[{}] [{}]", hashKey, verifyHash);

        return verifyHash;

    }
}
