package com.baidu.shop.entity;

import com.baidu.shop.validata.group.BaiduOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName CategryEntity
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/27
 * @Version V1.09999999999999999
 **/
@ApiModel(value = "category_model")
@Table(name = "tb_category")
public class CategoryEntity {


    @Id
    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "不能为空id",groups = BaiduOperation.Update.class)
    private Integer id;


    @ApiModelProperty(value = "name")
    @NotEmpty(message = "不能为空 name",groups = {BaiduOperation.Add.class})
    private String name;

    @ApiModelProperty(value = "父级分类",example = "1")
    @NotNull(message = "不能为空 parentId",groups = {})
    private Integer parentId;

    @ApiModelProperty(value = "是否是父级节点",example = "1")
    @NotNull(message = "不能为空 isParent",groups = {BaiduOperation.Add.class,BaiduOperation.Update.class})
    private Integer isParent;


    @NotNull(message = "不能为空排序 sort",groups = {BaiduOperation.Add.class,BaiduOperation.Update.class})
    @ApiModelProperty(value = "排序",example = "1")
    private Integer sort;


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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
