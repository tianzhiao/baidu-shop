package com.baidu.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * @ClassName PayInfoDTO
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/22
 * @Version V1.09999999999999999
 **/
@Data
@ApiModel(value = "支付数据传输")
public class PayInfoDTO {

    @ApiModelProperty(value = "订单编号",example = "1")
    @NotNull(message = "订单编号不能为空")
    private Long orderId;

    @ApiModelProperty(value = "总金额")
    private String totalAmount;

    @ApiModelProperty(value = "订单名称")
    private String orderName;

    @ApiModelProperty(value = "订单详情")
    private String description;


}
