package com.baidu.shop.service.impl;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CityEntity;
import com.baidu.shop.mapper.CityMapper;
import com.baidu.shop.service.CItyService;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CityServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/23
 * @Version V1.09999999999999999
 **/

@RestController
public class CityServiceImpl extends BeanApiService implements CItyService {

    @Resource
    private CityMapper cityMapper;
    @Override
    public Result<List<CityEntity>> getCity(Long pid) {
        Example example = new Example(CityEntity.class);
        example.createCriteria().andEqualTo("parentId",pid);
        List<CityEntity> cityEntities = cityMapper.selectByExample(example);
        return this.setResultSuccess(cityEntities);
    }
}
