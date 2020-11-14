package com.baidu.shop.feign;

import com.baidu.shop.service.SpecificationService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SpecParamFeign
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/20
 * @Version V1.09999999999999999
 **/
@FeignClient(value = "xxx-service",contextId = "SpecificationService")
public interface SpecParamFeign extends SpecificationService {
}
