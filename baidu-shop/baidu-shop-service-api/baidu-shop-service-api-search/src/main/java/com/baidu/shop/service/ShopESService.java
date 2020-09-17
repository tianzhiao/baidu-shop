package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName ShopESService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/16
 * @Version V1.09999999999999999
 **/
@Api(tags = "ES测试接口")
public interface ShopESService {

    @ApiOperation(value = "openfeign测试")
    @GetMapping("/test")
    Result<JsonObject> test();
}
