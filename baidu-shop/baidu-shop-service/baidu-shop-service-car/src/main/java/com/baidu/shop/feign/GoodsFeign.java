package com.baidu.shop.feign;

import com.baidu.shop.service.GoodsService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName GoodsFeign
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/19
 * @Version V1.09999999999999999
 **/
@FeignClient(value = "xxx-service",contextId = "GoodsService")
public interface GoodsFeign extends GoodsService {
}
