package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.document.GoodsDocument;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpecParamsDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SpecParamsEntity;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.feign.SearchFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.service.ShopESService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utlis.JSONUtil;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ShopElasticsearchServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/16
 * @Version V1.09999999999999999
 **/
@RestController
public class ShopElasticsearchServiceImpl extends BeanApiService implements ShopESService {

    @Resource
    private SearchFeign searchFeign;

    @Resource
    private SpecificationFeign specificationFeign;

    @Override
    public Result<JsonObject> test() {

        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setRows(5);
        spuDTO.setPage(1);
        Result<List<SpuDTO>> listSpuResult = searchFeign.list2(spuDTO);
        if (listSpuResult.getCode() == HttpStatus.SC_OK) {

            listSpuResult.getData().stream().forEach(spuDTO1 -> {

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

                Map<List<Long>, List<Map<String, Object>>> listListMap = this.getPriceAndSku(spuDTO1.getId());

                listListMap.forEach((key ,value) -> {
                    // 将sku数据转成json字符串放进Document中
                    goodsDocument.setSkus(JSONObject.toJSONString(value));
                    goodsDocument.setPrice(key);
                });

                Map<String, Object> hashMapSpecs = this.getHashMapSpecs(spuDTO1);

                goodsDocument.setSpecs(hashMapSpecs);

                System.out.println(goodsDocument);

            });

        }

        return null;
    }

    private Map<List<Long>,  List<Map<String, Object>>> getPriceAndSku(Integer spuId){


        Map<List<Long>,  List<Map<String, Object>>> listMapMap = new HashMap<>();

        Map<String, Object>  skus  = new HashMap<>();
        //装的是price全部数据
        List<Long> prices = new ArrayList<>();

        //遍历sku数据
        List<Map<String, Object>> collect = null;
        Result<List<SkuDTO>> skuResultList = searchFeign.stockAndSku(spuId);

        if(skuResultList.getCode() == HttpStatus.SC_OK){
            collect = skuResultList.getData().stream().map(sku -> {

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

                    if(params.getSearching() && params.getSegments() != null){
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
//            System.out.println(detailStrMap);
//            System.out.println(detailListMap);--

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
