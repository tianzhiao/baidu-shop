package com.baidu.shop.mapper;

import com.baidu.shop.entity.StockEntity;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName StockMapper
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/8
 * @Version V1.09999999999999999
 **/

public interface StockMapper extends Mapper<StockEntity>, DeleteByIdListMapper<StockEntity,Long> {

    @Update(value = "UPDATE tb_stock \n" +
            "SET stock = ( SELECT * FROM ( SELECT stock FROM tb_stock WHERE sku_id = #{skuId} ) AS a ) - #{num} \n" +
            "WHERE\n" +
            "\tsku_id = #{skuId}")
    void editStockTableOfStock(Long skuId, Long num);
}
