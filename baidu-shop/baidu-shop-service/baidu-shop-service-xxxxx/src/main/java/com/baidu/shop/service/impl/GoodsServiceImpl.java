package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.dto.SpuDetailDTO;
import com.baidu.shop.entity.SkuEntity;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.entity.SpuEntity;
import com.baidu.shop.entity.StockEntity;
import com.baidu.shop.mapper.*;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.utlis.BaiduBeanUtil;
import com.baidu.shop.utlis.StringUtil;
import com.github.pagehelper.PageHelper;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
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
public class GoodsServiceImpl extends BeanApiService implements GoodsService {

    @Resource
    private SpuMapper spuMapper;

    @Resource
    private BrandMapper brandMapper;

    //分类
    @Resource
    private CategoryMapper categoryMapper;

    //库存
    @Resource
    private StockMapper stockMapper;

    @Resource
    private SkuMapper skuMapper;

    //细节
    @Resource
    private SpuDetailMapper spuDetailMapper;

    @Transactional
    @Override
    public Result<Result<JSONObject>> add(SpuDTO spuDTO) {


        Date date = new Date();
        /**
         *
         * spu 新增
         */
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        //创建时间
        spuEntity.setLastUpdateTime(date);
        spuEntity.setCreateTime(date);
        //附上默认值 1是上架
        spuEntity.setValid(1);
        // 1    有效
        spuEntity.setSaleable(1);
        spuMapper.insertSelective(spuEntity);

        //spuID 主键
        Integer spuID = spuEntity.getId();
        /**
         * spuDetailMapper (细节)
         */
        SpuDetailEntity spuDetailEntity = BaiduBeanUtil.copyProperties(spuDTO.getSpuDetail(), SpuDetailEntity.class);
        spuDetailEntity.setSpuId(spuID);
        spuDetailMapper.insertSelective(spuDetailEntity);

        spuDTO.getSkus().stream().forEach(skuDTO -> {

            /**
             *  吧skuDTO数据copy 到 SkuEntity
             */
            SkuEntity skuEntity = BaiduBeanUtil.copyProperties(skuDTO, SkuEntity.class);
            //付默认值 --> 也就是创建这条数据的时间
            skuEntity.setCreateTime(date);
            skuEntity.setLastUpdateTime(date);
            //附上spu 主键 -- 》 id
            skuEntity.setSpuId(spuID);
            // 新增
            skuMapper.insertSelective(skuEntity);
            //创建一个 stockEntity 也就是 new 一个stockEntity对象
            StockEntity stockEntity = new StockEntity();

            //吧 sku新增返回的主键付给 setSkuId
            stockEntity.setSkuId(skuEntity.getId());
            //把前台传递过来的数据放到skuDTO 再放到 setStock里面
            stockEntity.setStock(skuDTO.getStock());

            //新增
            stockMapper.insertSelective(stockEntity);
        });

        return this.setResultSuccess();
    }

    /**
     * 查询
     * @param spuDTO
     * @return
     */
    @Override
    public Result<Map<String, Object>> list(SpuDTO spuDTO) {

        //if (StringUtil.isIntNotNull(spuDTO.getPage()))spuDTO.setPage((spuDTO.getPage() -1) * spuDTO.getRows());
        if(StringUtil.isIntNotNull(spuDTO.getRows()) && StringUtil.isIntNotNull(spuDTO.getRows()))
            PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());

        List<SpuDTO> listBySpuId = spuMapper.getListBySpuId(spuDTO);

        Integer count = spuMapper.count(spuDTO);


        Map<String, Object> map = new HashMap<>();
        map.put("total",count);
        map.put("list",listBySpuId);
        return this.setResultSuccess(map);

       // return this.setResult(HTTPStatus.OK,"" + count,listBySpuId);
    }

    @Override
    public Result<SpuDetailEntity> detail(Integer spuId) {

        SpuDetailEntity spuDetailEntity = spuDetailMapper.selectByPrimaryKey(spuId);
        return this.setResultSuccess(spuDetailEntity);
    }

    @Override
    public Result<List<SkuDTO>> stockAndSku(Integer spuId) {
        List<SkuDTO> stockAndSku = skuMapper.stockAndSku(spuId);
        return this.setResultSuccess(stockAndSku);
    }

    @Transactional
    @Override
    public Result<Result<JSONObject>> edit(SpuDTO spuDTO) {

        System.out.println(spuDTO);

        Date date = new Date();
        // 修改spu
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setLastUpdateTime(date);
        spuMapper.updateByPrimaryKeySelective(spuEntity);

        //修改 detail
        spuDetailMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(spuDTO.getSpuDetail(),SpuDetailEntity.class));

        this.del(spuDTO.getId());
        /*Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuDTO.getId());
        //先把sku 数据删除掉 && stock
        List<Long> skuId = skuMapper.selectByExample(example).stream().map(s -> s.getId()).collect(Collectors.toList());


        if(skuId != null && skuId.size() != 0){

            skuMapper.deleteByIdList(skuId);
            //删除 stock 数据
            stockMapper.deleteByIdList(skuId);
        }*/

        spuDTO.getSkus().stream().forEach(sku -> {
            SkuEntity skuEntity = BaiduBeanUtil.copyProperties(sku, SkuEntity.class);

            skuEntity.setLastUpdateTime(date);
            skuEntity.setCreateTime(date);
            skuEntity.setSpuId(spuDTO.getId());
            skuMapper.insertSelective(skuEntity);
            StockEntity stockEntity = new StockEntity();
            stockEntity.setStock(sku.getStock());
            stockEntity.setSkuId(skuEntity.getId());
            stockMapper.insertSelective(stockEntity);
        });


        return this.setResultSuccess();
    }

    private void del(Integer spuId){
        Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuId);

        List<Long> skuId = skuMapper.selectByExample(example).stream().map(s -> s.getId()).collect(Collectors.toList());


        if(skuId != null && skuId.size() != 0){

            skuMapper.deleteByIdList(skuId);
            //删除 stock 数据
            stockMapper.deleteByIdList(skuId);
        }

    }

    @Transactional
    @Override
    public Result<JSONObject> delete(Integer spuId) {

        //删除 spu
        spuMapper.deleteByPrimaryKey(spuId);
        //删除 detail
        spuDetailMapper.deleteByPrimaryKey(spuId);
        //删除 sku
        //删除 stock
        this.del(spuId);

        return this.setResultSuccess();
    }

    /**
     * 商品下架
     * @param
     * @return
     */
    @Transactional
    @Override
    public Result<JsonObject> outOf(SpuEntity spuEntity) {

        if(spuEntity.getSaleable() != null && spuEntity.getSaleable() == 1){
            spuEntity.setSaleable(0);
        }else {
            spuEntity.setSaleable(1);
        }
        spuMapper.updateByPrimaryKeySelective(spuEntity);

        return this.setResultSuccess();
    }


    /**
     *   List<SpuDTO> listBySpuId = spuMapper.getListBySpuId(spuDTO);
     *
     *    Integer count = spuMapper.count(spuDTO);
     *    干掉 下面的代码
     */

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
}
