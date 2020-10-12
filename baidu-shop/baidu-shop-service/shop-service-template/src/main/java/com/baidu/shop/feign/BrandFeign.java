package com.baidu.shop.feign;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.service.BrandService;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @ClassName BrandFeign
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/21
 * @Version V1.09999999999999999
 **/
@FeignClient(value = "xxx-service",contextId = "BrandServices")
public interface BrandFeign extends BrandService {

}
