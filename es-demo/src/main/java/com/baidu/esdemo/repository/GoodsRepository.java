package com.baidu.esdemo.repository;

import com.baidu.esdemo.entity.GoodsEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @ClassName GoodsRepostiory
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/14
 * @Version V1.09999999999999999
 **/

public interface GoodsRepository extends ElasticsearchRepository<GoodsEntity,Long>{


    List<GoodsEntity> findByBrandAndAndCategory(String brand,String category);
}
