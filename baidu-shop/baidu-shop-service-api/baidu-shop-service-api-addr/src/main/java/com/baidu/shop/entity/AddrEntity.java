package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName AddrEntity
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/23
 * @Version V1.09999999999999999
 **/

@Data
@Table(name = "tb_addr")
public class AddrEntity {

    @Id
    private Long id;

    private Long userId;

    private String name;

    private String phone;

    private String state;

    private String district;

    private String address;

    private String zipCode;

    private Boolean defaults;

    private String city;

}
