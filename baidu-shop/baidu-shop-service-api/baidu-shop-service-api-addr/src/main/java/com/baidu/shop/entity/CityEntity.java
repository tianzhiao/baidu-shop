package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName CityEntity
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/23
 * @Version V1.09999999999999999
 **/

@Table(name = "t_city")
@Data
public class CityEntity {

    @Id
    private Integer id;

    private String name;

    private String parentId;

    private Integer level;
}
