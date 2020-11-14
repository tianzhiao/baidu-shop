package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.constant.UserConstant;
import com.baidu.shop.dto.UserDTO;
import com.baidu.shop.entity.UserEntity;
import com.baidu.shop.mapper.UserMapper;
import com.baidu.shop.redis.RedisRepository;
import com.baidu.shop.service.UserService;
import com.baidu.shop.utlis.BCryptUtil;
import com.baidu.shop.utlis.BaiduBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/13
 * @Version V1.09999999999999999
 **/
@RestController
@Slf4j
public class UserServiceImpl extends BeanApiService implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisRepository redisRepository;

    @Override
    public Result<JSONObject> register(UserDTO userDTO) {

        UserEntity userEntity = BaiduBeanUtil.copyProperties(userDTO, UserEntity.class);

        userEntity.setCreated(new Date());

        userEntity.setPassword(BCryptUtil.hashpw(userEntity.getPassword(),BCryptUtil.gensalt()));

        userMapper.insertSelective(userEntity);

        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> checkUsernameOrPhone(String value, Integer type) {
        Example example = new Example(UserEntity.class);
        Example.Criteria criteria = example.createCriteria();

        if(type == UserConstant.USER_VALUE_USERNAME){
            criteria.andEqualTo("username",value);
        }else if (type == UserConstant.USER_VALUE_PHONE){
            criteria.andEqualTo("phone",value);
        }

        List<UserEntity> userEntities = userMapper.selectByExample(example);
        return this.setResultSuccess(userEntities);
    }

    @Override
    public Result<JSONObject> sent(UserDTO userDTO) {
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        //LuosimaoDuanxinUtil.sendSpeak(userDTO.getPhone(),code);
        log.debug("phone : {} , code : {} ",userDTO.getPhone(),code);

        //将数据保存到redis数据库
        redisRepository.set("valid-code-" + userDTO.getPhone(),code);

        //将redis数据设置为300秒 300秒以后自动失效
        redisRepository.expire("valid-code-" + userDTO.getPhone(),300);

        return this.setResultSuccess(code);
    }

    @Override
    public Result<JSONObject> userCheckVerificationCode(String phone, String verification) {

        String verificationCode = redisRepository.get("valid-code-" + phone);

        if(verificationCode.equals(verification)) return this.setResultSuccess();

        return this.setResultError("验证码错误");
    }
}
