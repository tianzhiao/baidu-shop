package com.baidu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ClassName EurekaServerApplication8761
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/27
 * @Version V1.09999999999999999
 **/

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication8761 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication8761.class, args);
    }

}
