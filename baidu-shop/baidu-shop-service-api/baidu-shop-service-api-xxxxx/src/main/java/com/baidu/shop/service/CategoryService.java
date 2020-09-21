package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.validata.group.BaiduOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName CategoryService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/27
 * @Version V1.09999999999999999
 **/
@Api(value = "商品接口")
public interface CategoryService {

    @ApiOperation(value = "CategoryService 查询商品分类")
    @GetMapping("/cat/list")
    Result<List<CategoryEntity>> getcategoryByParentId(Integer id);

    @ApiOperation(value = "CategoryService 查询商品分类")
    @GetMapping("/cat/getByBrand")
    Result<List<CategoryEntity>> getByBrand(Integer brandId);


    @ApiOperation(value = "CategoryService 新增")
    @PostMapping("/cat/save")     // {BaiduOperation.Add.class} 表示只验证 这个组的
    Result<JSONObject> save(@Validated({BaiduOperation.Add.class}) @RequestBody  CategoryEntity categoryEntity);

    @ApiOperation(value = "CategoryService 修改")
    @PutMapping("/cat/edit")
    Result<JSONObject> ecit(@Validated({BaiduOperation.Update.class}) @RequestBody  CategoryEntity categoryEntity);

    @ApiOperation(value = "CategoryService 删除")
    @DeleteMapping("/cat/delete")
    Result<JSONObject> save(Integer id);


    @ApiOperation(value = "通过分类主键去查询")
    @GetMapping("/cat/getCategoryById")
    Result<List<CategoryEntity>> getCategoryById(@RequestParam String catIdStrs);
}
