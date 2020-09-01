package com.baidu.shop.entity;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName CategoryBrandEntity
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/1
 * @Version V1.09999999999999999
 **/
@Table(name = "tb_category_brand")
public class CategoryBrandEntity {

    private Integer categoryId;

    private Integer brandId;


    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }
}
