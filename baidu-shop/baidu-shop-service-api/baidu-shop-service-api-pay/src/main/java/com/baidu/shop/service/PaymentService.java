package com.baidu.shop.service;

import com.baidu.shop.dto.PayInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName PaymentService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/22
 * @Version V1.09999999999999999
 **/
@Api(tags = "支付模块")
public interface PaymentService {

    @GetMapping("/payment/requestPayment")
    @ApiOperation(value = "支付请求")
    void requestPayment(PayInfoDTO payInfoDTO, HttpServletResponse httpServletResponse, HttpServletRequest request);

    @ApiOperation(value = "通知")
    @GetMapping("/payment/returnNotify")
    void returnNotify(HttpServletRequest request);

    @ApiOperation(value = "成功的请求")
    @GetMapping("/payment/returnUrl")
    void returnUrl(HttpServletRequest request,HttpServletResponse response);
}
