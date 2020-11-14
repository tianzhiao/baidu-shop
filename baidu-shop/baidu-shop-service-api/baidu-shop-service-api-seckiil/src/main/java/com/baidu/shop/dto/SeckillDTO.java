package com.baidu.shop.dto;

import lombok.Data;

/**
 * @ClassName SeckillDTO
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/27
 * @Version V1.09999999999999999
 **/

@Data
public class SeckillDTO {

    private Integer id;

    private String title;

    private Integer price;

    private String image;

    //秒杀总数
    private Integer SeckillCountStock;

}
