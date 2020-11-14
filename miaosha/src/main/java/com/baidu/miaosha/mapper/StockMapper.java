package com.baidu.miaosha.mapper;

import com.baidu.miaosha.entity.Stock;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName StockMMapper
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/27
 * @Version V1.09999999999999999
 **/

public interface StockMapper extends Mapper<Stock> {


    @Update(" update stock " +
            "set\n" +

            "      sale = sale + 1,\n" +
            "      version = version + 1\n" +
            "    WHERE id = #{id}\n" +
            "    AND version = #{version}")
    int updateStockByOptimistic(Stock stock);

    /*update stock
    <set>
    sale = sale + 1,
    </set>
    WHERE id = #{id,jdbcType=INTEGER}
    AND sale = #{sale,jdbcType=INTEGER}*/

    Stock getStockByIdForUpdate(int sid);

    @Update("update stock " +
            "set\n" +
            "    sale = sale + 1 \n" +
            "    WHERE id = #{id}\n" +
            "    AND sale = #{sale}")
    int updateStockByOptimistics(Stock stock);

}
