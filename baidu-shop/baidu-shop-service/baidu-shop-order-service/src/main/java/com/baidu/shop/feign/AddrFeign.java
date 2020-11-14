package com.baidu.shop.feign;

import com.baidu.shop.base.Result;
import com.baidu.shop.entity.AddrEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName AddrFeign
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/23
 * @Version V1.09999999999999999
 **/
@FeignClient(value = "addr-server",contextId = "AddrFeign")
public interface AddrFeign {

    @GetMapping("/get/addr")
    Result<AddrEntity> getAddrById(@RequestParam Long id);
}
