package com.baidu.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

/**
 * @ClassName UserDTO
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/15
 * @Version V1.09999999999999999
 **/
@Data
public class UserDTO {

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

    @ApiModelProperty(hidden = true,value = "验证码")
    private String Code;
}
