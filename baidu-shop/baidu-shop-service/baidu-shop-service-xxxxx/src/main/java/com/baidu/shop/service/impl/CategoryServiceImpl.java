package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CategoryServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/27
 * @Version V1.09999999999999999
 **/

@RestController
public class CategoryServiceImpl extends BeanApiService implements CategoryService {

    /**
     * CategoryService 接口
     * BeanApiService 封装了 返回的数据
     */
    @Resource
    private CategoryMapper mapper;

    @Override
    public Result<List<CategoryEntity>> getcategoryByParentId(Integer id) {

        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.setParentId(id);

        List<CategoryEntity> select = mapper.select(categoryEntity);

        return this.setResultSuccess(select);
    }

    @Override
    public Result<JSONObject> save(CategoryEntity categoryEntity) {

        CategoryEntity categoryEntity1 = new CategoryEntity();

        categoryEntity1.setIsParent(1);
        categoryEntity1.setId(categoryEntity.getParentId());
        mapper.updateByPrimaryKeySelective(categoryEntity1);

        mapper.insertSelective(categoryEntity);

        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> ecit(CategoryEntity categoryEntity) {

        mapper.updateByPrimaryKeySelective(categoryEntity);

        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> save(Integer id) {


        CategoryEntity categoryEntity = mapper.selectByPrimaryKey(id);

        if(null == categoryEntity){//创建
            return this.setResultError("没有-id");
        }

        Example example = new Example(CategoryEntity.class);
		//categoryEntity.getParentId() 是 id
        example.createCriteria().andEqualTo("parentId",categoryEntity.getId());
        List<CategoryEntity> categoryEntities = mapper.selectByExample(example);
        if(categoryEntities.size() == 1){

            CategoryEntity categoryEntity1 = mapper.selectByPrimaryKey(categoryEntities.get(0).getParentId());

            CategoryEntity categoryEntity2 = new CategoryEntity();

            categoryEntity2.setIsParent(0);
            categoryEntity2.setId(categoryEntity1.getParentId());
            mapper.updateByPrimaryKeySelective(categoryEntity2);

        }

        mapper.deleteByPrimaryKey(id);

        return this.setResultSuccess();
    }
}
