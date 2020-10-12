package com.baidu.shop.feign;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName SearchFeign
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/29
 * @Version V1.09999999999999999
 **/
@FeignClient(value = "search-server",contextId = "SearchFeign")
public interface SearchFeign {

    @ApiOperation(value = "新增数据search")
    @GetMapping("/es/getSearch")
    Result<JsonObject> getGoodsSearchSave(@RequestParam Integer spuId);

    @ApiOperation("删除Goods 数据根据主键")
    @DeleteMapping("/es/deleteSearchGoods")
    Result<JSONObject> deleteGoodsById(@RequestParam Integer id);
}
