package com.baidu.shop.service.impl;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.entity.SpuEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.mapper.SpuMapper;
import com.baidu.shop.service.SpuService;
import com.baidu.shop.utlis.BaiduBeanUtil;
import com.baidu.shop.utlis.ObjectUtil;
import com.baidu.shop.utlis.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName SpuServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/7
 * @Version V1.09999999999999999
 **/
@RestController
@Slf4j
public class SpuServiceImpl extends BeanApiService implements SpuService {

    @Resource
    private SpuMapper spuMapper;

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Result<Map<String, Object>> list(SpuDTO spuDTO) {

        //if (StringUtil.isIntNotNull(spuDTO.getPage()))spuDTO.setPage((spuDTO.getPage() -1) * spuDTO.getRows());
        if(StringUtil.isIntNotNull(spuDTO.getRows()) && StringUtil.isIntNotNull(spuDTO.getRows()))
            PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());

        List<SpuDTO> listBySpuId = spuMapper.getListBySpuId(spuDTO);

        Integer count = spuMapper.count(spuDTO);


     /*   if(ObjectUtil.isNotNull(spuDTO.getPage()) && ObjectUtil.isNotNull(spuDTO.getRows()))
            PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());

        Example example = new Example(SpuEntity.class);

        example.setOrderByClause(spuDTO.getOrderByClause());
        Example.Criteria criteria = example.createCriteria();

        if(ObjectUtil.isNotNull(spuDTO.getSaleable()))
            criteria.andEqualTo("saleable",spuDTO.getSaleable());

        if(!StringUtil.isEmpty(spuDTO.getTitle()))
            criteria.andLike("title","%" + spuDTO.getTitle() + "%");

        List<SpuEntity> spuEntities = spuMapper.selectByExample(example);


        List<SpuDTO> collect = spuEntities.stream().map(spu -> {

            SpuDTO spuDTO1 = BaiduBeanUtil.copyProperties(spu, SpuDTO.class);
            SpuDTO listBySpuId = brandMapper.getListBySpuId(spuDTO1.getId());
            log.info(": {}" ,listBySpuId);
//            SpuDTO sppppp =  brandMapper.getInfoBySpuIdAndBrandId(spu.getBrandId(),spu.getId());
//
//            log.info(" sppppp : {}",sppppp);
        *//*    BrandEntity brandEntity = brandMapper.selectByPrimaryKey(spuDTO1.getBrandId());

            spuDTO1.setBrandName(brandEntity.getName());
            String cat = categoryMapper.selectByIdList(
                    Arrays.asList(spuDTO1.getCid1(), spuDTO1.getCid2(), spuDTO1.getCid3())).stream().map(c ->
                    c.getName()).collect(Collectors.joining("/"));

            spuDTO1.setCategoryName(cat);*//*

            return listBySpuId;
        }).collect(Collectors.toList());

        PageInfo<SpuDTO> spuEntityPageInfo = new PageInfo(spuEntities);
*/
        Map<String, Object> map = new HashMap<>();
        map.put("total",count);
        map.put("list",listBySpuId);
        return this.setResultSuccess(map);
    }
}
