package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.UserDTO;
import com.baidu.shop.validata.group.BaiduOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName UserService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/13
 * @Version V1.09999999999999999
 **/

@Api(tags = "user 接口")
public interface UserService {

    @ApiOperation(value = "注册user")
    @PostMapping(value = "/user/register")
    Result<JSONObject> register(@Validated({BaiduOperation.Add.class}) @RequestBody UserDTO userDTO);

    @ApiOperation(value = "验证usernameOrphone")
    @GetMapping(value = "/user/check/{value}/{type}")
    Result<JSONObject> checkUsernameOrPhone(@PathVariable(value = "value") String value,@PathVariable(value = "type") Integer type);

    @ApiOperation(value = "发送验证码")
    @PostMapping(value = "/user/send")
    Result<JSONObject> sent(@RequestBody UserDTO userDTO);

    @ApiOperation(value = "校验验证码")
    @GetMapping(value = "/user/verification")
    Result<JSONObject> userCheckVerificationCode(String phone,String verification);


}
