package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.Car;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName CarService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/19
 * @Version V1.09999999999999999
 **/
@Api(tags = "购物车接口")
public interface CarService {

    @ApiOperation(value = "添加购物车")
    @PostMapping("/car/addCar")
    Result<JSONObject> addCar(@RequestBody Car car, @CookieValue(value = "MRSHOP_TOKEN") String token);


    @ApiOperation(value = "合并购物车")
    @PostMapping("/car/mergeCar")
    Result<JsonObject> mergeCar(@RequestBody String clientCarList, @CookieValue(value = "MRSHOP_TOKEN") String token);

    @ApiOperation(value = "查询购物车数据")
    @GetMapping("/get/car")
    Result<List<Car>> getCar(@CookieValue(value = "MRSHOP_TOKEN") String token);

    @ApiOperation(value = "更改num值")
    @GetMapping("/car/toNumUpdate")
    Result<JSONObject> numUpdate(@CookieValue(value = "MRSHOP_TOKEN") String token,Long skuId,Integer type);

    @ApiModelProperty(value = "删除的goodsCar数据")
    @GetMapping("/car/delGoodsCar")
    Result<List<Car>> delGoodsCar(@CookieValue(value = "MRSHOP_TOKEN") String token,String skuId);
}
