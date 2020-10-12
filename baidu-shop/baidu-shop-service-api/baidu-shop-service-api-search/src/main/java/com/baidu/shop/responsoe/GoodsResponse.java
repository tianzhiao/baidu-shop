package com.baidu.shop.responsoe;

import com.baidu.shop.base.Result;
import com.baidu.shop.document.GoodsDocument;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName GoodsResponse
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/21
 * @Version V1.09999999999999999
 **/
@Data
@NoArgsConstructor
public class GoodsResponse extends Result<List<GoodsDocument>> {

    private Long total;

    private Long totalPage;

    private List<CategoryEntity> catList;

    private List<BrandEntity> brandList;


    private HashMap<String, List<String>> specResultList;

    public GoodsResponse(Long total,Long totalPage,List<CategoryEntity> catList ,List<BrandEntity> brandList,List<GoodsDocument> goodsDocuments,HashMap<String, List<String>> specResultList){

        super(HttpStatus.SC_OK, HttpStatus.SC_OK + "", goodsDocuments);

        this.total = total;
        this.brandList = brandList;
        this.catList = catList;
        this.totalPage = totalPage;
        this.specResultList = specResultList;
    }

}
