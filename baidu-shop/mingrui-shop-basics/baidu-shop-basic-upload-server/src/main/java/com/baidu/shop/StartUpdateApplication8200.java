package com.baidu.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName StartUpdateApplication8200
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/1
 * @Version V1.09999999999999999
 **/

@SpringBootApplication
@EnableEurekaClient
public class StartUpdateApplication8200 {

    public static void main(String[] args) {
        SpringApplication.run(StartUpdateApplication8200.class, args);
    }
}
