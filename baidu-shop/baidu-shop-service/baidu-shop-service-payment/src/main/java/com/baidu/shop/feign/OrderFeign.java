package com.baidu.shop.feign;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.OrderInfo;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName feignOrderFeign
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/22
 * @Version V1.09999999999999999
 **/
@FeignClient(value = "order-server",contextId = "feignOrderFeign")
public interface OrderFeign {

    @ApiOperation(value = "根据指定的orderId查询订单信息")
    @GetMapping("/order/getOrderInfoByOrderId")
    Result<OrderInfo> getOrderByOrderId(@RequestParam Long orderId);

    @ApiOperation(value = "更改状态")
    @GetMapping("/order/editStatus")
    Result<JSONObject> editStatus(@RequestParam String orderId);
}
