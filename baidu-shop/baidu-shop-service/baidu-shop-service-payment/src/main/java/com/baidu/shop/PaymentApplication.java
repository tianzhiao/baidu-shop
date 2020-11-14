package com.baidu.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName PaymentApplication
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/22
 * @Version V1.09999999999999999
 **/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableEurekaClient
public class PaymentApplication {


    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}
