package com.baidu.shop.service.impl;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import org.springframework.web.bind.annotation.RestController;

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
}
