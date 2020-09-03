package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.dto.SpecParamsDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpecParamsEntity;
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
    Result<List<SpecGroupEntity>> list(@Validated({BaiduOperation.Update.class}) SpecGroupEntity specificationDTO);

    @PostMapping("/specification/save")
    @ApiOperation(value = "新增")
    Result<JsonObject> save(@Validated({BaiduOperation.Add.class})  @RequestBody SpecGroupDTO specGroupDTO);

    @PutMapping("/specification/save")
    @ApiOperation(value = "edit")
    Result<JsonObject> edit(@Validated({BaiduOperation.Update.class}) @RequestBody SpecGroupDTO specGroupDTO);


    @DeleteMapping("/specification/delete")
    @ApiOperation(value = "delete")
    Result<JsonObject> delete(Integer id);

    @ApiOperation(value = "参数查询")
    @GetMapping("/specification/ParamsLoadList")
    Result<List<SpecParamsEntity>> list(SpecParamsDTO specParamsDTO);

    @ApiOperation(value = "参数新增")
    @PostMapping("/specification/ParamsSaveOrUpdate")
    Result<JsonObject> save(@Validated({BaiduOperation.Add.class}) @RequestBody SpecParamsDTO specParamsDTO);

    @ApiOperation(value = "参数修改")
    @PutMapping("/specification/ParamsSaveOrUpdate")
    Result<JsonObject> edit(@Validated({BaiduOperation.Update.class}) @RequestBody SpecParamsDTO specParamsDTO);

    @ApiOperation(value = "参数删除")
    @DeleteMapping("/specification/paramsDelete")
    Result<JsonObject> delete(Long id);

}
