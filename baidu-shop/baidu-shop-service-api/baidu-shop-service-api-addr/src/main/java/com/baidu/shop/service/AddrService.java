package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.AddrDTO;
import com.baidu.shop.entity.AddrEntity;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @ClassName AddrService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/23
 * @Version V1.09999999999999999
 **/

public interface AddrService {

    @GetMapping("/addr/getAddr")
    @ApiOperation(value = "通过用户查询地址信息")
    Result<List<AddrEntity>> getAddrInfo(@CookieValue("MRSHOP_TOKEN") String token);

    @ApiOperation(value = "新增地址")
    @PostMapping("/addr/saveAddr")
    Result<JSONObject> saveAddr(@CookieValue("MRSHOP_TOKEN") String token,@RequestBody AddrDTO addrDTO);

    @ApiOperation(value = "修改地址")
    @PutMapping("/addr/saveAddr")
    Result<JSONObject> updateAddr(AddrDTO addrDTO);

    @ApiOperation(value = "删除地址")
    @DeleteMapping("/addr/deleteAddr")
    Result<JSONObject> deleteAddr(Long id);

    @GetMapping("/get/addr")
    Result<AddrEntity> getAddrById(Long id);
}
