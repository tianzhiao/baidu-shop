package com.baidu.shop.service.impl;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.entity.SkuEntity;
import com.baidu.shop.entity.StockEntity;
import com.baidu.shop.mapper.SeckillMapper;
import com.baidu.shop.mapper.StockMapper;
import com.baidu.shop.service.SeckillService;
import com.baidu.shop.utlis.BaiduBeanUtil;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SeckillServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/

@RestController
public class SeckillServiceImpl extends BeanApiService implements SeckillService {

    @Resource
    private SeckillMapper seckillMapper;

    @Resource
    private StockMapper stockMapper;

    @Override
    public Result<List<SkuDTO>> getSeckillData() {

        List<SkuDTO> skuDTOS = new ArrayList<>();

        Example example = new Example(StockEntity.class);
        example.createCriteria().andNotEqualTo("seckillStock",0);
        List<StockEntity> stockEntities = stockMapper.selectByExample(example);

        stockEntities.stream().forEach(stockEntity ->{
            SkuEntity skuEntity = seckillMapper.selectByPrimaryKey(stockEntity.getSkuId());

            if(skuEntity != null){
                SkuDTO skuDTO = BaiduBeanUtil.copyProperties(skuEntity, SkuDTO.class);
                if(stockEntity.getSeckillTotal() != null)
                    skuDTO.setSeckillTotal(stockEntity.getSeckillTotal());
                if(stockEntity.getSeckillStock() != null)
                    skuDTO.setSeckillStock(stockEntity.getSeckillStock());
                skuDTOS.add(skuDTO);
            }

        });

        return this.setResultSuccess(skuDTOS);
    }
}
