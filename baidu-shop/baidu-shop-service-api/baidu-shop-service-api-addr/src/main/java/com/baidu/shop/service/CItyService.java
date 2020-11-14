package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CityEntity;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @ClassName CItyService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/23
 * @Version V1.09999999999999999
 **/
public interface CItyService {

    @GetMapping("/addr/getCity")
    Result<List<CityEntity>> getCity(Long pid);
}
