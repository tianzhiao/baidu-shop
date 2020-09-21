package com.baidu.shop.feign;

import com.baidu.shop.service.CategoryService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName CategoryFeign
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/21
 * @Version V1.09999999999999999
 **/
@FeignClient(value = "xxx-service",contextId = "CategoryService")
public interface CategoryFeign extends CategoryService {
}
