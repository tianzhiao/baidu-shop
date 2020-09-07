package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.mapper.SpecGroupMapper;
import com.baidu.shop.service.CategoryService;
import com.baidu.shop.utlis.ObjectUtil;
import com.baidu.shop.utlis.StringUtil;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private SpecGroupMapper specGroupMapper;

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

    /**
     *  Category 分类管理 --> 删除
     * @param id
     * @return
     */
    String msg = "";
    @Override
    public Result<JSONObject> save(Integer id) {



        CategoryEntity categoryEntity = mapper.selectByPrimaryKey(id);
        if(null == categoryEntity){//创建
            return this.setResultError("没有-id");
        }
        if(categoryEntity.getIsParent() == 1){
            return this.setResultError("父级节点不能被删除");
        }


        //通过 分组ID 去查询 中间表，
        //判断 id 是不是空
        if(StringUtil.isIntNotNull(id)){
            //每次清空
            msg = "";
            //通过商品主键查询中间表 的数据
            Example example1 = new Example(CategoryBrandEntity.class);
            example1.createCriteria().andEqualTo("categoryId",id);
            List<CategoryBrandEntity> categoryBrandEntities = categoryBrandMapper.selectByExample(example1);
            ArrayList<BrandEntity> brandEntities = new ArrayList<>();
            //可能是多个 ，遍历查询
            if(ObjectUtil.isNotEmpty(categoryBrandEntities)){
                for (CategoryBrandEntity categoryBrandEntity : categoryBrandEntities) {
                    //通过 List<CategoryBrandEntity> 返回的数据，查询 brandMapper（品牌表）
                    BrandEntity brandEntity = brandMapper.selectByPrimaryKey(categoryBrandEntity.getBrandId());
                    //返回的数据放到list 集合中 && 把BrandEntity的作用往上提也可以但是我没 式
                    brandEntities.add(brandEntity);
                }
            }
            //判断 ArrayList<BrandEntity> 是否有值
            if(ObjectUtil.isNotEmpty(brandEntities)){
                //其实这个可以不写forEach ，因为只会返回一条数据 可以这样写brandEntities.get(0)
                for (BrandEntity brandEntity : brandEntities) {
                    //拼接字符串
                    msg += " " + brandEntity.getName();
                }
                // 如果不是空就会拼接
                msg += "   brand(品牌)    ";
            }
            //通过 商品主键查询
            Example example = new Example(SpecGroupEntity.class);
            example.createCriteria().andEqualTo("cid",id);
            List<SpecGroupEntity> specGroupEntities = specGroupMapper.selectByExample(example);
            //判断是否为空
            if(ObjectUtil.isNotEmpty(specGroupEntities)){
                //遍历 List<SpecGroupEntity>
                for (SpecGroupEntity specGroupEntity : specGroupEntities) {
                    //拼接字符串
                    msg += " " + specGroupEntity.getName();
                }
                //不为空在拼接的字符串最后添加
                msg += "  group(分组)  ";
            }
            //判断字符长度
            if(msg.length() != 0){
                msg += "绑定不能被删除";
                //响应 被谁谁 绑定不能被删除
                return this.setResultError(msg);
            }
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


    @Override
    public Result<List<CategoryEntity>> getByBrand(Integer brandId) {

        List<CategoryEntity> list = mapper.getByBrand(brandId);
        return this.setResultSuccess(list);
    }
}
