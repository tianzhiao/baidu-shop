package com.baidu.shop.dto;

import com.baidu.shop.utlis.BaiduBeanUtil;
import com.baidu.shop.validata.group.BaiduOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName OrderDTO
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/21
 * @Version V1.09999999999999999
 **/

@Data
@ApiModel(value = "订单传输数据")
public class OrderDTO {

    @ApiModelProperty(value = "收货地址Ip",example = "1")
    @NotNull(message = "收获地址不能为空",groups = {BaiduOperation.Add.class})
    private Integer addrId;

    @ApiModelProperty(value = "以什么方式付钱",example = "1")
    @NotNull(message = "payType不能为空",groups = {BaiduOperation.Add.class})
    private Integer payType;

    @ApiModelProperty(value = "商品主键")
    @NotEmpty(message = "商品主键不能为空",groups = {BaiduOperation.Add.class})
    private String skuId;

}
