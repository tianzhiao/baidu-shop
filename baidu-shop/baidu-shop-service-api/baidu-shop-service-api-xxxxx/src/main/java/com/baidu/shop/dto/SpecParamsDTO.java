package com.baidu.shop.dto;

import com.baidu.shop.validata.group.BaiduOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName SpecParamsDTO
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/3
 * @Version V1.09999999999999999
 **/
@ApiModel(value = "规格参数组下的参数名")
@Data
public class SpecParamsDTO {

    @ApiModelProperty(value = "规格主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {BaiduOperation.Update.class})
    private Long id;

    @ApiModelProperty(value = "商品分类ID",example = "1")
//    @NotNull(message = "商品主键不能为空",groups = {BaiduOperation.Update.class,BaiduOperation.Add.class})
    private Long cid;

    @ApiModelProperty(value = "分组主键",example = "1")
//    @NotNull(message = "分类主键不能为空",groups = {BaiduOperation.Update.class,BaiduOperation.Add.class})
    private Long groupId;

    @ApiModelProperty(value = "参数名")
    @NotEmpty(message = "参数名不能为空",groups = {BaiduOperation.Update.class,BaiduOperation.Add.class})
    private String name;

    @ApiModelProperty(value = "是否是数字类型参数，true或false")
    @NotNull(message = "数字类型参数不能为空",groups = {BaiduOperation.Update.class,BaiduOperation.Add.class})
    private Boolean numeric;

    @ApiModelProperty(value = "数字类型参数的单位，非数字类型可以为空")
    private String unit;

    @ApiModelProperty(value = "是否是sku通用属性，true或false")
    @NotNull(message = "是否是sku通用属性不能为空",groups = {BaiduOperation.Update.class,BaiduOperation.Add.class})
    private Boolean generic;

    @ApiModelProperty(value = "是否用于搜索过滤，true或false")
    @NotNull(message = "是否用于搜索过滤不能为空",groups = {BaiduOperation.Update.class,BaiduOperation.Add.class})
    private Boolean searching;

    @ApiModelProperty(value = "数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0")
    private String segments;

}
