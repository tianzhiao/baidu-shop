package com.baidu.shop.entity;



import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName BrandEntity
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/31
 * @Version V1.09999999999999999
 **/
@Data
@Table(name = "tb_brand")
public class BrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //新增返回主键
    private Integer id;

    private String name;

    private String image;

    private Character letter;

}
