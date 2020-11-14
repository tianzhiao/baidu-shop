package com.baidu.shop.business.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.OrderDTO;
import com.baidu.shop.dto.OrderInfo;
import com.baidu.shop.entity.OrderEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName OrderService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/21
 * @Version V1.09999999999999999
 **/

@Api(tags = "订单模块")
public interface OrderService {

    @PostMapping("/order/createOrder")
    @ApiOperation(value = "创建订单")
    Result<String> createOrder(@RequestBody OrderDTO orderDTO, @CookieValue("MRSHOP_TOKEN") String token);

    @ApiOperation(value = "根据指定的orderId查询订单信息")
    @GetMapping("/order/getOrderInfoByOrderId")
    Result<OrderInfo> getOrderByOrderId(@RequestParam Long orderId);

    @ApiOperation(value = "更改状态")
    @GetMapping("/order/editStatus")
    Result<JSONObject> editStatus(String orderId);
}
