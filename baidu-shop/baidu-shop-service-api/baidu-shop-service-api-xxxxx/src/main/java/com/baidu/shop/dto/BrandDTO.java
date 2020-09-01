package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.validata.group.BaiduOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName BrandDTO
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/31
 * @Version V1.09999999999999999
 **/
@ApiModel(value = "商品DTO")
public class BrandDTO extends BaseDTO {

    @ApiModelProperty(value = "商品 ID",example = "1")
    @NotNull(message = "主键不能为空" ,groups = {BaiduOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "商品名字")
    @NotEmpty(message = "名字不能空",groups = {BaiduOperation.Add.class})
    private String name;

    @ApiModelProperty(value = "商品图")
    private String image;

    @ApiModelProperty(value = "商品首字母")
    private Character letter;

    @ApiModelProperty(value = "商品和category信息")
    @NotEmpty(message = "category不能空",groups = {BaiduOperation.Add.class})
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Character getLetter() {
        return letter;
    }

    public void setLetter(Character letter) {
        this.letter = letter;
    }
}
