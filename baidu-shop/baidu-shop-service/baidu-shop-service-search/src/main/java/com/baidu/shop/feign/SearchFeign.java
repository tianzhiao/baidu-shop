package com.baidu.shop.feign;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.service.ShopESService;
import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;

import javax.annotation.Resource;
import java.util.Map;

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
