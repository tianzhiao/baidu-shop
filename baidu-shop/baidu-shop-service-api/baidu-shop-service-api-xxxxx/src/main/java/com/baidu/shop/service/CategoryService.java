package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @ClassName CategoryService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/27
 * @Version V1.09999999999999999
 **/
@Api(value = "商品接口")
public interface CategoryService {

    @ApiOperation(value = "CategoryService 查询商品分类")
    @GetMapping("/cat/list")
    Result<List<CategoryEntity>> getcategoryByParentId(Integer id);


}
