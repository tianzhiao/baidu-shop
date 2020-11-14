package com.baidu.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName RunAddrApplication
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/23
 * @Version V1.09999999999999999
 **/
@MapperScan(value = "com.baidu.shop.mapper")
@SpringBootApplication
@EnableEurekaClient
public class RunAddrApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunAddrApplication.class, args);
    }
}
