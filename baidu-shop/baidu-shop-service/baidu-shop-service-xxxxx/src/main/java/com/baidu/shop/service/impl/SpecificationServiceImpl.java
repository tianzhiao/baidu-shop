package com.baidu.shop.service.impl;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.dto.SpecParamsDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpecParamsEntity;
import com.baidu.shop.mapper.SpecGroupMapper;
import com.baidu.shop.mapper.SpecParamsMapper;
import com.baidu.shop.service.SpecificationService;
import com.baidu.shop.utlis.BaiduBeanUtil;
import com.baidu.shop.utlis.ObjectUtil;
import com.baidu.shop.utlis.StringUtil;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;
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
    private SpecGroupMapper mapper;

    @Resource
    private SpecParamsMapper paramsMapper;

    @Override
    public Result<List<SpecGroupEntity>> list(SpecGroupEntity specificationDTO) {

        Example example = new Example(SpecGroupEntity.class);

        if(specificationDTO.getCid() != null) example.createCriteria().andEqualTo("cid",specificationDTO.getCid());

        List<SpecGroupEntity> specificationEntities = mapper.selectByExample(example);

        return this.setResultSuccess(specificationEntities);
    }

    @Transactional
    @Override
    public Result<JsonObject> save(SpecGroupDTO specGroupDTO) {

        mapper.insertSelective(BaiduBeanUtil.copyProperties(specGroupDTO, SpecGroupEntity.class));
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JsonObject> edit(SpecGroupDTO specGroupDTO) {

        mapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(specGroupDTO, SpecGroupEntity.class));
        return  this.setResultSuccess();
    }

    String msg = "";

    @Transactional
    @Override
    public Result<JsonObject> delete(Integer id) {
        msg = "";
        Example example = new Example(SpecParamsEntity.class);
        example.createCriteria().andEqualTo("groupId",id);
        List<SpecParamsEntity> specParamsEntities = paramsMapper.selectByExample(example);
        if(!ObjectUtil.isNotEmpty(specParamsEntities)) mapper.deleteByPrimaryKey(id);
        else this.setResultError("被 " + specParamsEntities.get(0).getName() + " 绑定，不能删除");
        return this.setResultSuccess();
    }
    //************************************params*********************************************//

    @Override
    public Result<List<SpecParamsEntity>> list(SpecParamsDTO specParamsDTO) {

        //if(specParamsDTO.getGroupId() == null) return this.setResultError("无效的ID");

        Example example = new Example(SpecParamsEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(null != specParamsDTO.getGroupId())
            criteria.andEqualTo("groupId",specParamsDTO.getGroupId());
        if(null != specParamsDTO.getCid())
            criteria.andEqualTo("cid",specParamsDTO.getCid());

        if(specParamsDTO.getSearching() != null)
            criteria.andEqualTo("searching",specParamsDTO.getSearching());

        if(specParamsDTO.getGeneric() != null)
            criteria.andEqualTo("generic",specParamsDTO.getGeneric());
        List<SpecParamsEntity> specParamsEntities = paramsMapper.selectByExample(example);

        return this.setResultSuccess(specParamsEntities);
    }

    @Transactional
    @Override
    public Result<JsonObject> save(SpecParamsDTO specParamsDTO) {
        paramsMapper.insertSelective(BaiduBeanUtil.copyProperties(specParamsDTO,SpecParamsEntity.class));

        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JsonObject> edit(SpecParamsDTO specParamsDTO) {

        paramsMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(specParamsDTO,SpecParamsEntity.class));

        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JsonObject> delete(Long id) {

        paramsMapper.deleteByPrimaryKey(id);

        return this.setResultSuccess();
    }
}
