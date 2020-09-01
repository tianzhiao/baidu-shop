package com.baidu.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @ClassName StartZuulApplication
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/28
 * @Version V1.09999999999999999
 **/

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class StartZuulApplication8088 {

    public static void main(String[] args) {
        SpringApplication.run(StartZuulApplication8088.class, args);
    }
}
