package com.baidu.shop.feign;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName StockFeign
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/22
 * @Version V1.09999999999999999
 **/
@FeignClient(value = "xxx-service",contextId = "StockFeign")
public interface StockFeign {

    @ApiOperation(value = "更新stock")
    @GetMapping("/stock/edit")
    Result<JSONObject> edit(@RequestParam Long skuId, @RequestParam Long num);
}
