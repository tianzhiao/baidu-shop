package com.baidu.shop.service.impl;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.entity.SpuEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.mapper.SpuMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utlis.BaiduBeanUtil;
import com.baidu.shop.utlis.PinyinUtil;
import com.baidu.shop.utlis.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName BrandServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/31
 * @Version V1.09999999999999999
 **/
@RestController
public class BrandServiceImpl extends BeanApiService implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    @Resource
    private SpuMapper spuMapper;


    @Override
    public Result<List<BrandEntity>> list(Integer cid) {

        List<BrandEntity>  list = brandMapper.brandByCategoryId(cid);

        return this.setResultSuccess(list);
    }

    @Override
    @ResponseBody
    public Result<PageInfo<List<BrandEntity>>> getBrandInfo(BrandDTO brandDTO) {

        if(brandDTO.getPage() != null && brandDTO.getRows() != null)
            PageHelper.startPage(brandDTO.getPage(), brandDTO.getRows());

        Example example = new Example(BrandEntity.class);
        Example.Criteria criteria = example.createCriteria();

        if (brandDTO.getDescending() != null) example
                .setOrderByClause(brandDTO.getOrderByClause());
        if(StringUtil.isIntNotNull(brandDTO.getId()))
            criteria.andEqualTo("id",brandDTO.getId());

        if (null != brandDTO.getName() && !"".equals(brandDTO.getName()))
            criteria.andLike("name", "%" + brandDTO.getName() + "%");

        List<BrandEntity> brandEntities = brandMapper.selectByExample(example);


        PageInfo<List<BrandEntity>> pageInfo = new PageInfo(brandEntities);

        Result<PageInfo<List<BrandEntity>>> pageInfoResult = new Result<>();
        pageInfoResult.setCode(200);
        pageInfoResult.setMessage("success");
        pageInfoResult.setData(pageInfo);

        return pageInfoResult;
    }

    @Override
    public BrandEntity getBrandInfo2(BrandDTO brandDTO) {
        return brandMapper.selectByPrimaryKey(brandDTO.getId());
    }

    @Override
    public Result<List<BrandEntity>> getBrandById(String brandIdStrs) {

        List<Integer> brandIdList = Arrays.asList(brandIdStrs.split(",")).
                stream().map(brandId -> Integer.parseInt(brandId)).collect(Collectors.toList());
        List<BrandEntity> brandEntities = brandMapper.selectByIdList(brandIdList);

        return this.setResultSuccess(brandEntities);
    }

    @Transactional
    @Override
    public Result<JsonObject> save(BrandDTO brandDTO) {


        //获取商品名字
//        String name = brandDTO.getName();
//        //把商品名字转换成 拼音
//        String upperCase = PinyinUtil.getUpperCase(name, PinyinUtil.TO_FIRST_CHAR_PINYIN).charAt(0);
//        //只要拼音的首字母
//        char c = upperCase.charAt(0);
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

        brandEntity.setLetter(PinyinUtil.getUpperCase(brandDTO.getName(), PinyinUtil.TO_FIRST_CHAR_PINYIN).charAt(0));

        brandMapper.insertSelective(brandEntity);

        //将关系存储到数据库

        this.insertCategoryAndBrand(brandDTO,brandEntity);

      /*  if (brandDTO.getCategory().contains(",")) {

            //brandDTO.getCategory().split(",") 分割字符串返回数组
            //Arrays.asList(brandDTO.getCategory().split(",")) 把数组放到List集合中
            //stream() 调用 JDK1.8
            //map 可以 return 新的数据
            //collect（） 转换返回类型
            //Collectors.toList() 设置返回类型
            List<CategoryBrandEntity> collect = Arrays.asList(brandDTO.getCategory().split(",")).stream().map(cat -> {

                CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();

                categoryBrandEntity.setBrandId(brandEntity.getId());

                categoryBrandEntity.setCategoryId(StringUtil.paramsInteger(cat));

                return categoryBrandEntity;

            }).collect(Collectors.toList());
            //批量新增 mapper 需要继承 InsertListMapper <T>
            categoryBrandMapper.insertList(collect);

        } else {

            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();

            categoryBrandEntity.setBrandId(brandEntity.getId());

            categoryBrandEntity.setCategoryId(StringUtil.paramsInteger(brandDTO.getCategory()));

            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }
*/

        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JsonObject> edit(BrandDTO brandDTO) {

        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().charAt(0)), PinyinUtil.TO_FIRST_CHAR_PINYIN).charAt(0));

        brandMapper.updateByPrimaryKeySelective(brandEntity);

        //之前删除关系表中的数据

        Example example = new Example(CategoryBrandEntity.class);

        //通过 brandId 主键删除关系表中的数据
        example.createCriteria().andEqualTo("brandId", brandDTO.getId());

        categoryBrandMapper.deleteByExample(example);

        this.insertCategoryAndBrand(brandDTO,brandEntity);

        /*if (brandDTO.getCategory().contains(",")) {

            //批量增
            List<CategoryBrandEntity> collect = Arrays.asList(brandDTO.getCategory().split(",")).stream().map(v -> {

                CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();

                categoryBrandEntity.setBrandId(brandEntity.getId());

                categoryBrandEntity.setCategoryId(StringUtil.paramsInteger(v));

                return categoryBrandEntity;

            }).collect(Collectors.toList());

            categoryBrandMapper.insertList(collect);

        } else {
            //单个增

            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();

            categoryBrandEntity.setBrandId(brandEntity.getId());

            categoryBrandEntity.setCategoryId(StringUtil.paramsInteger(brandDTO.getCategory()));

            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }*/

        return this.setResultSuccess();
    }

    /**
     * 删除
     * @param id
     * @return
     */

    @Transactional
    @Override
    public Result<JsonObject> delete(Integer id) {

        Example example = new Example(SpuEntity.class);
        example.createCriteria().andEqualTo("brandId",id);
        List<SpuEntity> spuEntities = spuMapper.selectByExample(example);
        if (spuEntities.size() > 0) {
            return this.setResultError("不能删除有spu商品绑定");
        }

        brandMapper.deleteByPrimaryKey(id);
        this.deleteCategoryAndBrand(id);

        return this.setResultSuccess();
    }

    private void deleteCategoryAndBrand(Integer id){

        Example example = new Example(CategoryBrandEntity.class);

        //通过 brandId 主键删除关系表中的数据
        example.createCriteria().andEqualTo("brandId", id);

        categoryBrandMapper.deleteByExample(example);
    }


    private void insertCategoryAndBrand(BrandDTO brandDTO, BrandEntity brandEntity){
        if (brandDTO.getCategory().contains(",")) {

            //批量增
            List<CategoryBrandEntity> collect = Arrays.asList(brandDTO.getCategory().split(",")).stream().map(v -> {

                CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();

                categoryBrandEntity.setBrandId(brandEntity.getId());

                categoryBrandEntity.setCategoryId(StringUtil.paramsInteger(v));

                return categoryBrandEntity;

            }).collect(Collectors.toList());

            categoryBrandMapper.insertList(collect);

        } else {
            //单个增

            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();

            categoryBrandEntity.setBrandId(brandEntity.getId());

            categoryBrandEntity.setCategoryId(StringUtil.paramsInteger(brandDTO.getCategory()));

            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }
    }
}
