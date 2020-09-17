package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.entity.SpuEntity;
import com.baidu.shop.validata.group.BaiduOperation;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SpuService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/7
 * @Version V1.09999999999999999
 **/
@Api(tags = "spu 接口")
public interface GoodsService {

    @ApiOperation(value = "spu 查询")
    @GetMapping("/spu/list")
    Result<Map<String, Object>> list(@SpringQueryMap SpuDTO spuDTO);

    @ApiOperation(value = "spu 查询")
    @GetMapping("/spu/list2")
    Result<List<SpuDTO>> list2(@SpringQueryMap SpuDTO spuDTO);

    @ApiOperation(value = "spu 写入")
    @PostMapping("/spu/addInfo")
    Result<Result<JSONObject>> add(@Validated({BaiduOperation.Add.class}) @RequestBody  SpuDTO spuDTO);

    @ApiOperation(value = "spu 写入")
    @PutMapping("/spu/addInfo")
    Result<Result<JSONObject>> edit(@Validated({BaiduOperation.Update.class}) @RequestBody  SpuDTO spuDTO);

    @ApiOperation(value = "通过spuId 去查询 detail --> derail主键就是spuId")
    @GetMapping("/goods/spu/detail")
    Result<SpuDetailEntity> detail(@RequestParam Integer spuId);


    @ApiOperation(value = "通过spuId 去查询 stockAndsku")
    @GetMapping("/goods/sku/stock")
    Result<List<SkuDTO>> stockAndSku(@RequestParam Integer spuId);

    @ApiOperation(value = "通过spuId 去删除")
    @DeleteMapping("/goods/sku/delete")
    Result<JSONObject> delete(Integer spuId);

    @ApiOperation(value = "修改状态--下架")
    @PutMapping("/goods/outof")
    Result<JsonObject> outOf(@RequestBody SpuEntity spuEntity);
}
