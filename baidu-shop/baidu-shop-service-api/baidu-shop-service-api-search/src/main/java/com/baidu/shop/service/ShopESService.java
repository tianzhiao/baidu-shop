package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.responsoe.GoodsResponse;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  /*  @ApiOperation(value = "openfeign测试")
    @GetMapping("/test")
    Result<JsonObject> test();*/

    @ApiOperation(value = "删除es库")
    @GetMapping("/es/delete")
    Result<JSONObject> deleteESData();

    @ApiOperation(value = "添加es数据")
    @GetMapping("/es/saveAll")
    Result<JSONObject> saveESData();


    @ApiOperation(value = "查询")
    @GetMapping("/es/search")
    GoodsResponse esSearch(String search, Integer page,String filter);

    @ApiOperation(value = "新增数据search")
    @GetMapping("/es/getSearch")
    Result<JsonObject> getGoodsSearchSave(Integer spuId);

    @ApiOperation("删除Goods 数据根据主键")
    @DeleteMapping("/es/deleteSearchGoods")
    Result<JSONObject> deleteGoodsById(Integer id);
}
