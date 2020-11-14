package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.utlis.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @ClassName SeckillService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/27
 * @Version V1.09999999999999999
 **/

@Api(tags = "秒杀接口")
public interface SeckillService {

    @ApiOperation(value = "秒杀数据")
    @GetMapping("/seckill/getSeckillData")
    Result<List<SkuDTO>> getSeckillData();
}
