package com.baidu.shop.service.impl;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecificationDTO;
import com.baidu.shop.entity.SpecificationEntity;
import com.baidu.shop.mapper.SpecificationMapper;
import com.baidu.shop.service.SpecificationService;
import com.baidu.shop.utlis.BaiduBeanUtil;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SpecificationServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/3
 * @Version V1.09999999999999999
 **/
@RestController
public class SpecificationServiceImpl extends BeanApiService implements SpecificationService {


    @Resource
    private SpecificationMapper mapper;

    @Override
    public Result<List<SpecificationEntity>> list(SpecificationEntity specificationDTO) {

        Example example = new Example(SpecificationEntity.class);

        if(specificationDTO.getCid() != null) example.createCriteria().andEqualTo("cid",specificationDTO.getCid());

        List<SpecificationEntity> specificationEntities = mapper.selectByExample(example);

        return this.setResultSuccess(specificationEntities);
    }

    @Override
    public Result<JsonObject> save(SpecificationDTO specificationDTO) {

        mapper.insertSelective(BaiduBeanUtil.copyProperties(specificationDTO,SpecificationEntity.class));
        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> edit(SpecificationDTO specificationDTO) {

        mapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(specificationDTO,SpecificationEntity.class));
        return  this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> delete(Integer id) {

        mapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }
}
