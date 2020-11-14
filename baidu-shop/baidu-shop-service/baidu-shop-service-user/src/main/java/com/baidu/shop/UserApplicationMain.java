package com.baidu.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName UserApplicationMain
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/13
 * @Version V1.09999999999999999
 **/

@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = "com.baidu.shop.mapper")
public class UserApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(UserApplicationMain.class, args);
    }
}
