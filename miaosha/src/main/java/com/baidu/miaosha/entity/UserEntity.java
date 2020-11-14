package com.baidu.miaosha.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName UserEntity
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/

@Data
@Table(name = "test_user")
public class UserEntity {

    @Id
    private Integer id;
    private String userName;
}
