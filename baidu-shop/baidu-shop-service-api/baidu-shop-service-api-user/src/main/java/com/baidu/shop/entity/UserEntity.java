package com.baidu.shop.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName UserEntity
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/13
 * @Version V1.09999999999999999
 **/
@ApiModel(value = "用来登录")
@Table(name = "tb_user")
@Data
public class UserEntity {

    @Id
    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "创建时间")
    private Date created;

    @ApiModelProperty(value = "盐")
    private String salt;


}
