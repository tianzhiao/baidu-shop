package com.baidu.shop.entity;

/**
 * @ClassName SpecificationEntity
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/3
 * @Version V1.09999999999999999
 **/

import io.swagger.annotations.ApiModel;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_spec_group")
public class  SpecGroupEntity {

    @Id
    private  Integer id;

    private Integer cid;

    private String name;

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
