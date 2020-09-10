package com.baidu.shop.mapper;

import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.BrandEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName BrandMapper
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/31
 * @Version V1.09999999999999999
 **/

public interface BrandMapper extends Mapper<BrandEntity> {


    @Select("select * from tb_brand b where b.id in (SELECT cd.brand_id FROM tb_category_brand cd where cd.category_id = #{cid})")
    List<BrandEntity> brandByCategoryId(Integer cid);
}
