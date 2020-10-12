package com.baidu.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName StsrtXXXXAplication8100
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/27
 * @Version V1.09999999999999999
 **/
@SpringBootApplication
@MapperScan(value = "com.baidu.shop.mapper")
@EnableEurekaClient
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableFeignClients
public class StsrtXXXXAplication8100 {

    public static void main(String[] args) {
        SpringApplication.run(StsrtXXXXAplication8100.class, args);
    }
}
