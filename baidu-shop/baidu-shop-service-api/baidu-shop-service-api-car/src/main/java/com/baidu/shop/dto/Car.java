package com.baidu.shop.dto;

import com.baidu.shop.validata.group.BaiduOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName Car
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/19
 * @Version V1.09999999999999999
 **/

@Data
@ApiModel(value = "购物车")
public class Car {

    @ApiModelProperty(example = "1",value = "用户主键")
    private Integer userId;

    @NotNull(groups = {BaiduOperation.Add.class},message = "skuId新增不能为空")
    @ApiModelProperty(example = "1",value = "规格参数主键")
    private Long skuId;

    @ApiModelProperty(value = "标题")
    @NotEmpty(groups = {BaiduOperation.Add.class},message = "title不能为空")
    private String title;

    @ApiModelProperty(value = "图片")
    @NotEmpty(groups = {BaiduOperation.Add.class},message = "image不能为空")
    private String image;

    @ApiModelProperty(value = "价格")
    @NotNull(message = "价格不能为空",groups = {BaiduOperation.Add.class})
    private Long price;

    @ApiModelProperty(value = "规格参数")
    @NotEmpty(message = "规格参数不能为空", groups = {BaiduOperation.Add.class})
    private String ownSpec;

    @ApiModelProperty(value = "总数")
    private Integer num;
}
