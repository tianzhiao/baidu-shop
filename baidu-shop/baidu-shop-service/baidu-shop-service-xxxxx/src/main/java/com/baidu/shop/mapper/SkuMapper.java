package com.baidu.shop.mapper;

import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.entity.SkuEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName SkuMapper
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/8
 * @Version V1.09999999999999999
 **/

public interface SkuMapper extends Mapper<SkuEntity>, DeleteByIdListMapper<SkuEntity,Long> {

    @Select("select * from tb_sku s ,tb_stock sto where s.id = sto.sku_id and s.spu_id = #{spuId}")
    List<SkuDTO> stockAndSku(Integer spuId);
}
