package com.baidu.shop.dto;

import com.baidu.shop.validata.group.BaiduOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName UserDTO
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/13
 * @Version V1.09999999999999999
 **/

@Data
@ApiModel(value = "user model")
public class UserDTO {

    @ApiModelProperty(example = "1",value = "主键")
    @NotNull(message = "不能为null",groups = BaiduOperation.Update.class)
    private Integer id;

    @ApiModelProperty(value = "账户")
    @NotEmpty(message = "账户不能为空",groups = {BaiduOperation.Add.class})
    private String username;

    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "密码不能为空",groups = {BaiduOperation.Add.class})
    private String password;

    @ApiModelProperty(value = "手机号")
    @NotEmpty(message = "手机号不能为空",groups = {BaiduOperation.Add.class})
    private String phone;

    @ApiModelProperty(value = "创建时间",hidden = true)
    private Date created;

    @ApiModelProperty(value = "严么",hidden = true)
    private String salt;

    @ApiModelProperty(hidden = true)
    private String code;

}
