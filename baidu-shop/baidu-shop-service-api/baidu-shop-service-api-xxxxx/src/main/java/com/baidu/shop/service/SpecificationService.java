package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecificationDTO;
import com.baidu.shop.entity.SpecificationEntity;
import com.baidu.shop.validata.group.BaiduOperation;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SpecificationService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/3
 * @Version V1.09999999999999999
 **/
@Api(tags = "规范接口")
public interface SpecificationService {

    @GetMapping("/specification/list")
    @ApiOperation(value = "查寻")
    Result<List<SpecificationEntity>> list(@Validated({BaiduOperation.Update.class})  SpecificationEntity specificationDTO);

    @PostMapping("/specification/save")
    @ApiOperation(value = "新增")
    Result<JsonObject> save(@Validated({BaiduOperation.Add.class})  @RequestBody SpecificationDTO specificationDTO);

    @PutMapping("/specification/save")
    @ApiOperation(value = "edit")
    Result<JsonObject> edit(@Validated({BaiduOperation.Update.class}) @RequestBody SpecificationDTO specificationDTO);


    @DeleteMapping("/specification/delete")
    @ApiOperation(value = "delete")
    Result<JsonObject> delete(Integer id);

}
