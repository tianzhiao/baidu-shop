package com.baidu.shop.feign;

import com.baidu.shop.service.GoodsService;
import com.baidu.shop.service.GoodsServiceI;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SearchFeign
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/16
 * @Version V1.09999999999999999
 **/
@FeignClient(contextId = "GoodsService",value = "xxx-service")
public interface SearchFeign extends GoodsService {


}
