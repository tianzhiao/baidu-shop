package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SpuEntity;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

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
public interface SpuService {

    @ApiOperation(value = "spu 查询")
    @GetMapping("/spu/list")
    Result<Map<String, Object>> list(SpuDTO spuDTO);
}
