package com.baidu.shop.web;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.business.UserService;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.constant.ValidConstant;
import com.baidu.shop.dto.UserDTO;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.UserEntity;
import com.baidu.shop.redis.RedisRepository;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.CookieUtils;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utils.RsaUtils;
import com.baidu.shop.utlis.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName UserController
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/15
 * @Version V1.09999999999999999
 **/
@Slf4j
@RestController
public class UserController extends BeanApiService {


    @Resource
    private UserService userService;

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private RedisRepository redisRepository;


    /**
     * 登录
     * @param userDTO
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @PostMapping("/oauth/login")
    public Result<JSONObject> login(@RequestBody UserDTO userDTO, HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse){

        String token = userService.login(userDTO,jwtConfig,redisRepository);

        if(StringUtils.isEmpty(token)) return this.setResultError(HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED,"账号或密码不对");

        CookieUtils.setCookie(httpServletRequest,httpServletResponse,jwtConfig.getCookieName(),token,true);

        return this.setResultSuccess();
    }

    /**
     * 每次刷新页面 更新过期时间cookie
     * @param token
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping("/oauth/verify")
    public Result<UserInfo> verify(@CookieValue(value = "MRSHOP_TOKEN") String token,HttpServletRequest httpServletRequest,
                                   HttpServletResponse httpServletResponse){

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());

            String newToken = JwtUtils.generateToken(userInfo, jwtConfig.getPrivateKey(), jwtConfig.getCookieMaxAge());

            //访问一次请求就重新设置已次cookie时间
            CookieUtils.setCookie(httpServletRequest,httpServletResponse,jwtConfig.getCookieName(),
                    newToken,jwtConfig.getCookieMaxAge(),true);

            return this.setResultSuccess(userInfo);
        } catch (Exception e) {
            return this.setResultError(HttpStatus.SC_FORBIDDEN,"验证失败 e：" + e.getMessage());
        }

    }

    /**
     * 发送验证码
     * @param phone
     * @return
     */

    @GetMapping("/oauth/sentVerification/{phone}")
    public Result<JSONObject> sentVerification(@PathVariable(value = "phone") String phone){

        UserEntity userByPhone = userService.getUserByPhone(phone);

        if(ObjectUtil.isObj(userByPhone)) this.setResultError(HTTPStatus.PHONE_ERROR,"没有该手机号");

        Integer code = (int) ((Math.random() * 9 + 1) * 100000);
        log.debug("验证码 : {}" ,code);
        redisRepository.set(ValidConstant.VALID_PHONE + phone,code.toString());
        redisRepository.expire(ValidConstant.VALID_PHONE + phone,120);

        return this.setResultSuccess(code);
    }


}
