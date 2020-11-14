package com.baidu.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName RunApplicationCar
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/19
 * @Version V1.09999999999999999
 **/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients
public class RunApplicationCar {

    public static void main(String[] args) {
        SpringApplication.run(RunApplicationCar.class, args);
    }
}
