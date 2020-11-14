package com.baidu.miaosha.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName Stock
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/27
 * @Version V1.09999999999999999
 **/
@Data
@Table(name = "stock")
public class Stock {

    @Id
    private Integer id;

    private String name;

    private Integer count;

    private Integer sale;

    private Integer version;
}
