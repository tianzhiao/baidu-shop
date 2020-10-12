package com.baidu.shop.dto;

import com.baidu.shop.entity.SpecParamsEntity;
import com.baidu.shop.validata.group.BaiduOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName SpecificationDTO
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/3
 * @Version V1.09999999999999999
 **/
@ApiModel(value = "规格参数的分组表，每个商品分类下有多个规格参数组")
public class SpecGroupDTO {

    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "主键不能为空",groups = BaiduOperation.Update.class)
    private  Integer id;

    @ApiModelProperty(value = "商品分类id，一个分类下有多个规格组",example = "1")
    @NotNull(message = "商品分类cid不能为空",groups = BaiduOperation.Add.class)
    private Integer cid;

    @ApiModelProperty(value = "规格组的名称")
    @NotEmpty(message = "商品分类id不能为空",groups = BaiduOperation.Add.class)
    private String name;

    @ApiModelProperty(hidden = true)
    private  List<SpecParamsEntity> specParamsEntityList;

    public List<SpecParamsEntity> getSpecParamsEntityList() {
        return specParamsEntityList;
    }

    public void setSpecParamsEntityList(List<SpecParamsEntity> specParamsEntityList) {
        this.specParamsEntityList = specParamsEntityList;
    }

    @ApiModelProperty(hidden = true)
    private List<SpecParamsEntity> specParamsList;

    public List<SpecParamsEntity> getSpecParamsList() {
        return specParamsList;
    }

    public void setSpecParamsList(List<SpecParamsEntity> specParamsList) {
        this.specParamsList = specParamsList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
