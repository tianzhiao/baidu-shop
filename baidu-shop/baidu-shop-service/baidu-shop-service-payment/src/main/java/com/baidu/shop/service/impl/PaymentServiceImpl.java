package com.baidu.shop.service.impl;



import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.config.AliPayConfig;
import com.baidu.shop.dto.OrderInfo;
import com.baidu.shop.dto.PayInfoDTO;
import com.baidu.shop.feign.OrderFeign;
import com.baidu.shop.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @ClassName PayServiveImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/22
 * @Version V1.09999999999999999
 **/
@Slf4j
@Controller
public class PaymentServiceImpl extends BeanApiService implements PaymentService {

//    @Resource
//    private AliPayConfig aliPayConfig;


    @Resource
    private OrderFeign orderFeign;

    @Override
    public void requestPayment(PayInfoDTO payInfoDTO, HttpServletResponse httpServletResponse, HttpServletRequest request) {


        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.gatewayUrl, AliPayConfig.app_id, AliPayConfig.merchant_private_key, "json", AliPayConfig.charset, AliPayConfig.alipay_public_key, AliPayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AliPayConfig.return_url);
        alipayRequest.setNotifyUrl(AliPayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = null;
        try {
            Result<OrderInfo> orderByOrderId = orderFeign.getOrderByOrderId(payInfoDTO.getOrderId());

            if (orderByOrderId.getCode() == HttpStatus.SC_OK) {

                OrderInfo orderResult = orderByOrderId.getData();

                List<String> title = orderResult.getOrderDetailList().stream().map(detail -> detail.getTitle()).collect(Collectors.toList());
                String t = String.join(",",title);
                // new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8")
                out_trade_no = payInfoDTO.getOrderId().toString();
                //付款金额，必填 new String(request.getParameter("WIDtotal_amount").getBytes("ISO-8859-1"),"UTF-8")
                String total_amount = Double.valueOf(orderResult.getActualPay() / 100) +  "";
                //订单名称，必填 new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8")
                String subject = t.length() > 20 ? t.substring(0,20) : t;
                //商品描述，可空   new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8")
                String body =  "";

                alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                        + "\"total_amount\":\""+ total_amount +"\","
                        + "\"subject\":\""+ subject +"\","
                        + "\"body\":\""+ body +"\","
                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = null;
        try {
            result = alipayClient.pageExecute(alipayRequest).getBody();
            httpServletResponse.setContentType("text/html; charset=utf-8");
            httpServletResponse.getWriter().print(result);
        } catch (AlipayApiException | IOException e) {
            e.printStackTrace();
        }

        //输出
       // out.println(result);

    }

    @Override
    public void returnNotify(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put(name, valueStr);
        }

        boolean signVerified = false; //调用SDK验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AliPayConfig.alipay_public_key, AliPayConfig.charset, AliPayConfig.sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (signVerified) {//验证成功
            //商户订单号
            try {
                String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
                //支付宝交易号
                String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

                //交易状态
                String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");


                if (trade_status.equals("TRADE_FINISHED")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序

                    //注意：
                    //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                } else if (trade_status.equals("TRADE_SUCCESS")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序

                    //注意：
                    //付款完成后，支付宝系统发送该交易状态通知
                } else {//验证失败
                    // out.println("fail");

                    //调试用，写文本函数记录程序运行情况是否正常
                    //String sWord = AlipaySignature.getSignCheckContentV1(params);
                    //AlipayConfig.logResult(sWord);
                }


                //out.println("success");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            //——请在这里编写您的程序（以下代码仅作参考）——

	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/

            //——请在这里编写您的程序（以上代码仅作参考）——
        }

    }


    @Override
    public void returnUrl(HttpServletRequest request,HttpServletResponse response) {

        try {
            //获取支付宝GET过来反馈信息
            Map<String,String> params = new HashMap<String,String>();
            Map<String,String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AliPayConfig.alipay_public_key, AliPayConfig.charset, AliPayConfig.sign_type); //调用SDK验证签名

            //——请在这里编写您的程序（以下代码仅作参考）—— 详情
            if(signVerified) {
                //商户订单号
                String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

                //支付宝交易号
                String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

                //付款金额
                String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
                log.debug("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);

                response.sendRedirect("http://www.baidushop.com/success.html?orderId=" + out_trade_no + "&totalPay=" + total_amount);

                Result<JSONObject> orderStatusResult = orderFeign.editStatus(out_trade_no);
                if (orderStatusResult.getCode() == HttpStatus.SC_OK) {
                    log.debug("状态更新成功");
                }
                // out.println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);
            }else {
               // out.println("验签失败");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
