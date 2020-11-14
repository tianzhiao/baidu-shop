package com.baidu.shop.business.impl;

import com.baidu.shop.business.UserService;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.constant.ValidConstant;
import com.baidu.shop.dto.UserDTO;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.UserEntity;
import com.baidu.shop.mapper.UserMapper;
import com.baidu.shop.redis.RedisRepository;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utlis.BCryptUtil;
import com.baidu.shop.utlis.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/15
 * @Version V1.09999999999999999
 **/

@Component
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    /**
     *  登录操作
     * @param userDTO
     * @param jwtConfig
     * @param redisRepository
     * @return
     */

    @Override
    public String login(UserDTO userDTO, JwtConfig jwtConfig, RedisRepository redisRepository) {

        String token = null;
        String msg = "";
        Map<String, String> map = new HashMap<>();
        Example example = new Example(UserEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(null != userDTO){

            if(!StringUtil.isEmpty(userDTO.getUsername())){

                criteria.andEqualTo("username",userDTO.getUsername());

                List<UserEntity> userEntities = getUserList(example);
                if(userEntities.size() == 1){
                    if (BCryptUtil.checkpw(userDTO.getPassword(),userEntities.get(0).getPassword())) {
                        token = getToken(userEntities.get(0), jwtConfig, token);

                    }else msg = "密码不正确";

                }else  msg = "没有该账号";

            }

            if(!StringUtils.isEmpty(userDTO.getPhone()) && !StringUtils.isEmpty(userDTO.getCode())){
                criteria.andEqualTo("phone",userDTO.getPhone());
                List<UserEntity> userList = getUserList(example);

                if(userList.size() == 1){
                    String verification = redisRepository.get(ValidConstant.VALID_PHONE + userDTO.getPhone());

                    if(userDTO.getCode().equals(verification)){

                        token = getToken(userList.get(0), jwtConfig, token);
                    }else msg = "验证码不对";
                }else msg = "没有该手机号";
            }
        }
        map.put(token,msg);
        return token;
    }

    /**
     *  通过private（私钥）获取token，
     * @param userDTO
     * @param jwtConfig
     * @param token
     * @return
     */
    private String getToken(UserEntity userDTO, JwtConfig jwtConfig, String token) {
        try {
            token = JwtUtils.generateToken(new UserInfo(userDTO.getId(), userDTO.getUsername()),
                    jwtConfig.getPrivateKey(), jwtConfig.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     *  查询
     * @param example
     * @return
     */
    private List<UserEntity> getUserList(Example example) {
        List<UserEntity> userEntities = userMapper.selectByExample(example);
        return userEntities;
    }

    /**
     *  发送验证码之前先去查询一下有没有该手机号
     * @param phone
     * @return
     */
    @Override
    public UserEntity getUserByPhone(String phone) {
        Example example = new Example(UserEntity.class);
        example.createCriteria().andEqualTo("phone",phone);
        List<UserEntity> userEntities = userMapper.selectByExample(example);
        if(userEntities.size() == 1) return userEntities.get(0);
        return null;
    }
}
