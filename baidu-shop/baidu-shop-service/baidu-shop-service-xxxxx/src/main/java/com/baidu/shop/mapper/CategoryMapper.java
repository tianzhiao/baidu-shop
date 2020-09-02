package com.baidu.shop.mapper;

import com.baidu.shop.entity.CategoryEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName CategoryMapper
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/27
 * @Version V1.09999999999999999
 **/

public interface CategoryMapper extends Mapper<CategoryEntity> {

    @Select("select c.name,c.id from tb_category c where c.id in (select tb.category_id from tb_category_brand tb where tb.brand_id = #{brandId})")
    List<CategoryEntity> getByBrand(Integer brandId);
}
