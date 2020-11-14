package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.document.GoodsDocument;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpecParamsDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.entity.SpecParamsEntity;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.feign.BrandFeign;
import com.baidu.shop.feign.CategoryFeign;
import com.baidu.shop.feign.SearchFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.responsoe.GoodsResponse;
import com.baidu.shop.service.ShopESService;
import com.baidu.shop.utlis.ESHighLightUtil;
import com.baidu.shop.utlis.JSONUtil;
import com.baidu.shop.utlis.StringUtil;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpStatus;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ShopElasticsearchServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/16
 * @Version V1.09999999999999999
 **/
@RestController
@Slf4j
public class ShopElasticsearchServiceImpl extends BeanApiService implements ShopESService {

    @Resource
    private SearchFeign searchFeign;

    @Resource
    private SpecificationFeign specificationFeign;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private BrandFeign brandFeign;

    @Resource
    private CategoryFeign categoryFeign;

    @Value("${tianzhiao.path}")
    private String  pathPage;


    @Override
    public Result<JSONObject> deleteGoodsById(Integer id) {

        elasticsearchRestTemplate.delete(id.toString(),GoodsDocument.class);

        //将静态化页面删除
        File file = new File(pathPage + id + ".html");
        //判断文件是否存在
        if (file.exists()) {
            // 删除文件
            file.delete();
            log.info("删除成功");
        }
        return this.setResultSuccess();
    }

    // ctrl + alt + B
    //ctrl + alt + M
    @Override
    public GoodsResponse esSearch(String search,Integer page,String filter) {

        if(StringUtil.isEmpty(search))   throw new RuntimeException("search 不能为空");

        //查询
        SearchHits<GoodsDocument> search1 = elasticsearchRestTemplate.search(this.getSearchQueryBuilder(search,page,filter).build(), GoodsDocument.class);
        //取分组
        Aggregations aggregations = search1.getAggregations();
        //brand 数据
        List<BrandEntity> brandList = this.getBrandList(aggregations);

        List<CategoryEntity> categoryList = null;
        //category 数据
        Map<Integer, List<CategoryEntity>> categoryListAndCategoryId = this.getCategoryList(aggregations);

        Long cid = 0L;

        for (Map.Entry<Integer, List<CategoryEntity>> integerListEntry : categoryListAndCategoryId.entrySet()) {
            cid = integerListEntry.getKey().longValue();
            categoryList = integerListEntry.getValue();
        }

        Long page1 = this.getPage(search1.getTotalHits());

        HashMap<String, List<String>> specResultList = this.getSpecResultList(cid, search);

        List<GoodsDocument> collect = ESHighLightUtil.getHighLightHit(search1.getSearchHits())
                .stream().map(thit -> thit.getContent()).collect(Collectors.toList());

        return new GoodsResponse(search1.getTotalHits(),page1,categoryList,brandList,collect,specResultList);
    }


    private  HashMap<String, List<String>> getSpecResultList(Long cid,String search) {

        SpecParamsDTO specParamsDTO = new SpecParamsDTO();
        specParamsDTO.setCid(cid);
        specParamsDTO.setSearching(true);
        Result<List<SpecParamsEntity>> list = specificationFeign.list(specParamsDTO);

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        queryBuilder.withQuery(QueryBuilders.multiMatchQuery(search,"title","categoryName","brandName"));

        queryBuilder.withPageable(PageRequest.of(0,1));

        if (list.getCode() == HttpStatus.SC_OK) {
            list.getData().stream().forEach(params ->{

                //specs.主屏幕尺寸（英寸）.keyword
                queryBuilder.addAggregation(AggregationBuilders.terms(params.getName()).field("specs." + params.getName() + ".keyword"));

            });

            SearchHits<GoodsDocument> search1 = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsDocument.class);

            Aggregations aggregations = search1.getAggregations();
            HashMap<String, List<String>> stringListHashMap = new HashMap<>();

            list.getData().stream().forEach(params -> {

                Terms aggregation = aggregations.get(params.getName());

                List<String> collect = aggregation.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
                stringListHashMap.put(aggregation.getName(),collect);

            });

            return stringListHashMap;
        }

        return null;

    }

    /**
     * 算出总条数
     * @param total
     * @return
     */
    private Long getPage(Long total){
        Double page2 = Math.ceil(total.doubleValue() / 10);
        Long totalPage = page2.longValue();
        return totalPage;
    }

    /**
     * 构建查询
     * @param search
     * @param page
     * @return
     */

    private NativeSearchQueryBuilder getSearchQueryBuilder(String search,Integer page,String filter){

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        if (!StringUtil.isEmpty(filter) && filter.length() > 2) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            Map<String, String> filterMap = JSONUtil.toMapValueString(filter);

            filterMap.forEach((key,value) -> {
                MatchQueryBuilder matchQueryBuilder = null;
                if(key.equals("brandId") || key.equals("cid3")){

                    matchQueryBuilder = QueryBuilders.matchQuery(key, value);

                }else{
                    matchQueryBuilder = QueryBuilders.matchQuery("specs." + key + ".keyword",value);
                }
                boolQueryBuilder.must(matchQueryBuilder);
            });

            nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
        }

        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(search,"title","categoryName","brandName"));

        nativeSearchQueryBuilder.withHighlightBuilder(ESHighLightUtil.getHighlightBuilder("title"));

        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("brandId_agg").field("brandId").size(10000));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("categoryId_agg").field("cid3").size(10000));

        nativeSearchQueryBuilder.withPageable(PageRequest.of(page -1,10));

        return nativeSearchQueryBuilder;

    }

    /**
     * 查询 brand 数据
     * @param aggregations
     * @return
     */

    private List<BrandEntity> getBrandList(Aggregations aggregations){

        Terms brandId_agg = aggregations.get("brandId_agg");

        List<String> brandIdList = brandId_agg.getBuckets()
                .stream().map(brandId -> brandId.getKeyAsString()).collect(Collectors.toList());

        //根据brandId 去查询 BrandList
        //将list集合转换成 用 逗号分隔的字符串
        Result<List<BrandEntity>> brandResult = brandFeign.getBrandById(String.join(",", brandIdList));

        return brandResult.getData();

    }

    /**
     *  category 数据
     * @param aggregations
     * @return
     */
    private Map<Integer, List<CategoryEntity>> getCategoryList(Aggregations aggregations){


        Map<Integer, List<CategoryEntity>> listListHashMap = new HashMap<>();
        Terms categoryId_agg = aggregations.get("categoryId_agg");

        List<Integer> hotCid = Arrays.asList(0);
        List<Long> maxCount = Arrays.asList(0L);


        // getBuckets（桶） 数据
        List<String> categoryIdList = categoryId_agg.getBuckets()
                .stream().map(catgeory -> {

                    if(catgeory.getDocCount() > maxCount.get(0)){
                        maxCount.set(0,catgeory.getDocCount());
                        hotCid.set(0,catgeory.getKeyAsNumber().intValue());
                    }
                    return catgeory.getKeyAsString();
                }).collect(Collectors.toList());

        Result<List<CategoryEntity>> categoryResult = categoryFeign.getCategoryById(String.join(",",categoryIdList));

        listListHashMap.put(hotCid.get(0),categoryResult.getData());

        return listListHashMap;
    }

    @Override
    public Result<JSONObject> deleteESData() {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsDocument.class);
        if(indexOperations.exists()){
            indexOperations.delete();
        }

        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> saveESData() {

        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsDocument.class);

        if(!indexOperations.exists()){
            indexOperations.create();
            indexOperations.createMapping();
        }

        List<GoodsDocument> goodsDocuments = this.esDocument(null);

        elasticsearchRestTemplate.save(goodsDocuments);

        return this.setResultSuccess();
    }

    /**
     * 新增es数据
     * @param spuId
     * @return
     */
    @Override
    public Result<JsonObject> getGoodsSearchSave(Integer spuId) {

        List<GoodsDocument> goodsDocuments = this.esDocument(spuId);
        System.out.println(goodsDocuments);
        try {
            elasticsearchRestTemplate.save(goodsDocuments);
            log.info("search 新增数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("新增数据失败哦 ： {}",e.getMessage());
        }

        return this.setResultSuccess();
    }

    public List<GoodsDocument> esDocument(Integer spuId) {

        SpuDTO spuDTO = new SpuDTO();
        if(spuId != null && spuId != 0) spuDTO.setId(spuId);
        List<GoodsDocument> goodsDocuments = new ArrayList<>();

        Result<List<SpuDTO>> listSpuResult = searchFeign.list2(spuDTO);
        if (listSpuResult.getCode() == HttpStatus.SC_OK) {

            listSpuResult.getData().stream().forEach(spuDTO1 -> {

                GoodsDocument goodsDocument = getGoodsDocument(spuDTO1);

                goodsDocuments.add(goodsDocument);
            });
        }
        return goodsDocuments;
    }

    private GoodsDocument getGoodsDocument(SpuDTO spuDTO1) {

        GoodsDocument goodsDocument = new GoodsDocument();

        goodsDocument.setBrandName(spuDTO1.getBrandName());
        goodsDocument.setCategoryName(spuDTO1.getCategoryName());
        goodsDocument.setBrandId(spuDTO1.getBrandId().longValue());
        goodsDocument.setCid1(spuDTO1.getCid1().longValue());
        goodsDocument.setCid2(spuDTO1.getCid2().longValue());
        goodsDocument.setCid3(spuDTO1.getCid3().longValue());
        goodsDocument.setCreateTime(spuDTO1.getCreateTime());
        goodsDocument.setId(spuDTO1.getId().longValue());
        goodsDocument.setSubTitle(spuDTO1.getSubTitle());
        goodsDocument.setTitle(spuDTO1.getTitle());

        //sku与price价格的数据
        Map<List<Long>, List<Map<String, Object>>> listListMap = this.getPriceAndSku(spuDTO1.getId());

        listListMap.forEach((key ,value) -> {
            // 将sku数据转成json字符串放进Document中
            goodsDocument.setSkus(JSONUtil.toJsonString(value));
            goodsDocument.setPrice(key);
        });

        //规格的数据
        Map<String, Object> hashMapSpecs = this.getHashMapSpecs(spuDTO1);

        goodsDocument.setSpecs(hashMapSpecs);
        return goodsDocument;
    }

    private Map<List<Long>,  List<Map<String, Object>>> getPriceAndSku(Integer spuId){

        Map<List<Long>,  List<Map<String, Object>>> listMapMap = new HashMap<>();

        //装的是price全部数据
        List<Long> prices = new ArrayList<>();

        //遍历sku数据
        List<Map<String, Object>> collect = null;
        Result<List<SkuDTO>> skuResultList = searchFeign.stockAndSku(spuId);

        if(skuResultList.getCode() == HttpStatus.SC_OK){
            collect = skuResultList.getData().stream().map(sku -> {
                Map<String, Object>  skus  = new HashMap<>();
                skus.put("id",sku.getId());
                skus.put("price", sku.getPrice());
                skus.put("images", sku.getImages());
                skus.put("title", sku.getTitle());
                prices.add(sku.getPrice().longValue());

                return skus;
            }).collect(Collectors.toList());
        }

        listMapMap.put(prices,collect);

        return listMapMap;
    }

    private  Map<String, Object> getHashMapSpecs(SpuDTO spuDTO1){


        SpecParamsDTO specParamsDTO = new SpecParamsDTO();
        //通过category查询params 参数
        specParamsDTO.setCid(spuDTO1.getCid3().longValue());
        Result<List<SpecParamsEntity>> list = specificationFeign.list(specParamsDTO);

        //查询detail数据
        Result<SpuDetailEntity> detail = searchFeign.detail(spuDTO1.getId());
        SpuDetailEntity data = detail.getData();
        //将detail通用数据转换成map
        Map<String, String> detailStrMap = JSONUtil.toMapValueString(data.getGenericSpec());

        //在将detail的特殊(Special)数据转换成map
        Map<String, List<String>> detailListMap = JSONUtil.toMapValueStrList(data.getSpecialSpec());

        Map<String, Object> hashMap = new HashMap<>();

        if (list.getCode() == HttpStatus.SC_OK) {

            list.getData().stream().forEach(params -> {
                //通用属性
                if(params.getGeneric()){

                    if(params.getNumeric()){
                        //处理区间查询
                        String s = this.chooseSegment(detailStrMap.get(params.getId().toString()), params.getSegments(), params.getUnit());
                        hashMap.put(params.getName(),s);
                    }else{
                        hashMap.put(params.getName(),detailStrMap.get(params.getId().toString()));
                    }

                }else{ //特殊属性

                    hashMap.put(params.getName(), detailListMap.get(params.getId().toString()));
                }
            });


        }

        return hashMap;
    }

    /**
     * 处理区间 --> 也就将区间查询传换成精准查询
     * @param value
     * @param segments
     * @param unit
     * @return
     */
    private String chooseSegment(String value, String segments, String unit) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : segments.split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + unit + "以上";
                }else if(begin == 0){
                    result = segs[1] + unit + "以下";
                }else{
                    result = segment + unit;
                }
                break;
            }
        }
        return result;
    }

}
