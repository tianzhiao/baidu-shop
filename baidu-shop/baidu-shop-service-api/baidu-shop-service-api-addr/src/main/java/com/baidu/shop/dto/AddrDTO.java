package com.baidu.shop.dto;

import com.baidu.shop.utlis.BaiduBeanUtil;
import com.baidu.shop.validata.group.BaiduOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName AddrDTO
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/23
 * @Version V1.09999999999999999
 **/

@ApiModel(value = "地址model")
@Data
public class AddrDTO {

    @ApiModelProperty(value = "地址主键",example = "1")
    @NotNull(message = "修改主键不能null",groups = {BaiduOperation.Update.class})
    private Long id;

    @ApiModelProperty(value = "用户主键",example = "1")
    private Long userId;

    @ApiModelProperty(value = "收货人")
    @NotEmpty(message = "收货人不能为空",groups = {BaiduOperation.Add.class})
    private String name;

    @ApiModelProperty(value = "收货人手机号")
    @NotEmpty(message = "手机号不能空",groups = {BaiduOperation.Add.class})
    private String phone;

    @ApiModelProperty(value = "省份")
    @NotEmpty(message = "省份",groups = {BaiduOperation.Add.class})
    private String state;

    @ApiModelProperty(value = "区")
    @NotEmpty(message = "district",groups = {BaiduOperation.Add.class})
    private String district;

    @ApiModelProperty(value = "街道")
    private String address;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "是否是默认数据")
    private Boolean defaults;

    @ApiModelProperty(value = "城市")
    private Integer city;

}
