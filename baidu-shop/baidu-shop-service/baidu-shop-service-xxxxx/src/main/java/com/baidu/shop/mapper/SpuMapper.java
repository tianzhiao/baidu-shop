package com.baidu.shop.mapper;

import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SpuEntity;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName SpuMapper
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/7
 * @Version V1.09999999999999999
 **/

public interface SpuMapper extends Mapper<SpuEntity> {

    List<SpuDTO> getListBySpuId(SpuDTO spuDTO);

    Integer count(SpuDTO spuDTO);
}
