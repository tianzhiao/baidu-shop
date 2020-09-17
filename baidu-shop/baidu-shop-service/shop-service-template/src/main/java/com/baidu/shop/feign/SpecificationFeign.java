package com.baidu.shop.feign;

import com.baidu.shop.service.SpecificationService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SpecificationFeign
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/17
 * @Version V1.09999999999999999
 **/
@FeignClient(contextId = "SpecificationService",value = "xxx-service")
public interface SpecificationFeign extends SpecificationService {
}
