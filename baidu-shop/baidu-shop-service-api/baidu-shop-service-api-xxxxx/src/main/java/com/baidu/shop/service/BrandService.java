package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.validata.group.BaiduOperation;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName BrandService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/31
 * @Version V1.09999999999999999
 **/

@Api(tags = "商品接口")
public interface BrandService {

    @ApiOperation(value = "商品GET方法")
    @GetMapping("/brand/getBrandInfo")
    Result<PageInfo<List<BrandEntity>>> getBrandInfo(BrandDTO brandDTO);

    @ApiOperation(value = "商品新增方法")
    @PostMapping("/brand/save")
    Result<JsonObject> save(@Validated({BaiduOperation.Add.class}) @RequestBody BrandDTO brandDTO);
}
