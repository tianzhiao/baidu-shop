package com.baidu.shop.service.impl;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.*;
import com.baidu.shop.entity.*;
import com.baidu.shop.feign.BrandFeign;
import com.baidu.shop.feign.SearchFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.service.GoodsServiceI;
import com.baidu.shop.utlis.BaiduBeanUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName GoodsServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/23
 * @Version V1.09999999999999999
 **/

@Service
public class GoodsServiceImpl implements GoodsServiceI {

    @Resource
    private SearchFeign searchFeign;
    @Resource
    private BrandFeign brandFeign;

    @Resource
    private SpecificationFeign specificationFeign;

    @Override
    public Map<String, Object> getGoodsBySpuId(Integer spuId) {

        Map<String, Object> map = new HashMap<>();
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setId(spuId);
        //spu 数据
        Result<List<SpuDTO>> listResult = searchFeign.list2(spuDTO);

        if(listResult.getCode() == 200){
            if(listResult.getData().size() == 1){
                SpuDTO spuDTO1 = listResult.getData().get(0);
                map.put("spuInfo",spuDTO1);
                //brand数据
                BrandEntity brandById = getBrandById(spuDTO1);
                map.put("brandInfo2",brandById);
            }

            Integer cid3 = listResult.getData().get(0).getCid3();

            SpuDetailEntity detailByCid3 = getDetailByCid3(spuId);
            map.put("spuDetail",detailByCid3);
            //根据 cid 查 group 分组
            List<SpecGroupEntity> specifcationByCid3 = getSpecifcationByCid3(cid3);
            map.put("groupList",specifcationByCid3);

            //特殊参数
            HashMap<Long, String> paramsDistinctiveByCid3 = getParamsDistinctiveByCid3(cid3);
            map.put("paramsMap",paramsDistinctiveByCid3);

            //sku 数据
            List<SkuDTO> skuBySpuId = getSkuBySpuId(spuId);
            map.put("skus",skuBySpuId);
            // ===========================================================================================================
            //分组数据与参数数据
            List<SpecGroupDTO> paramsAndGroup = getSpecGroupDTOS(cid3);

            map.put("paramsAndGroup",paramsAndGroup);

            // prams 数据 通用属性
            HashMap<Integer, String> integerStringHashMap = getParamsByCid3(cid3);

            map.put("paramsMy",integerStringHashMap);
        }

        return map;
    }

    private BrandEntity getBrandById(SpuDTO spuDTO1) {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(spuDTO1.getBrandId());
        //brand（商品数据） 数据
        BrandEntity brandInfo2 = brandFeign.getBrandInfo2(brandDTO);

        if (brandInfo2 != null) {
            return brandInfo2;
        }
        return null;
    }

    private SpuDetailEntity getDetailByCid3(Integer spuId) {
        Result<SpuDetailEntity> detail = searchFeign.detail(spuId);

        //spudetail 数据
        if (detail.getCode() == 200) {
            SpuDetailEntity data = detail.getData();
            return data;
        }
        return null;
    }

    /**
     * 根据 cid 查 group 分组
     * @param cid3
     * @return
     */
    private List<SpecGroupEntity> getSpecifcationByCid3(Integer cid3) {
        SpecGroupEntity specGroupEntity = new SpecGroupEntity();
        specGroupEntity.setCid(cid3);
        Result<List<SpecGroupEntity>> list = specificationFeign.list(specGroupEntity);

        if(list.getCode() == 200){
            List<SpecGroupEntity> data = list.getData();
            return data;
        }
        return null;
    }

    /**
     *特殊参数
     * @param cid3
     * @return
     */

    private HashMap<Long, String> getParamsDistinctiveByCid3(Integer cid3) {
        SpecParamsDTO specParamsDTO = new SpecParamsDTO();
        specParamsDTO.setCid(cid3.longValue());
        specParamsDTO.setGeneric(false); //特殊参数
        //params数据
        Result<List<SpecParamsEntity>> list1 = specificationFeign.list(specParamsDTO);
        if (list1.getCode() == 200) {
            List<SpecParamsEntity> paramList = list1.getData();

            HashMap<Long, String> paramsMap = new HashMap<>();

            paramList.stream().forEach(param -> {
                paramsMap.put(param.getId(),param.getName());
            });
            return paramsMap;
        }
        return null;
    }

    /**
     * sku 数据
     * @param spuId
     * @return
     */

    private List<SkuDTO> getSkuBySpuId(Integer spuId) {
        Result<List<SkuDTO>> skusResult = searchFeign.stockAndSku(spuId);
        if(skusResult.getCode() == 200){
            List<SkuDTO> skus = skusResult.getData();
            return skus;
        }

        return null;
    }

    /**
     *  分组数据与参数数据
     * @param cid3
     * @return
     */
    private List<SpecGroupDTO> getSpecGroupDTOS(Integer cid3) {
        SpecGroupEntity specGroupEntity1 = new SpecGroupEntity();
        specGroupEntity1.setCid(cid3);
        Result<List<SpecGroupEntity>> specGroupList = specificationFeign.list(specGroupEntity1);

        List<SpecGroupDTO> paramsAndGroup = specGroupList.getData().stream().map(group -> {
            SpecGroupDTO specGroupDTO = BaiduBeanUtil.copyProperties(group, SpecGroupDTO.class);

            SpecParamsDTO specParamsDTO2 = new SpecParamsDTO();
            specParamsDTO2.setGroupId(specGroupDTO.getId().longValue());
            specParamsDTO2.setGeneric(true);

            Result<List<SpecParamsEntity>> list4 = specificationFeign.list(specParamsDTO2);
            if (list4.getCode() == 200) {
                List<SpecParamsEntity> data = list4.getData();
                specGroupDTO.setSpecParamsList(data);
            }
            return specGroupDTO;
        }).collect(Collectors.toList());

        return paramsAndGroup;
    }

    /**
     *  prams 数据 通用属性
     * @param cid3
     * @return
     */

    private HashMap<Integer, String> getParamsByCid3(Integer cid3) {
        SpecParamsDTO specParamsDTO1 = new SpecParamsDTO();
        specParamsDTO1.setGeneric(true);
        specParamsDTO1.setCid(cid3.longValue());
        Result<List<SpecParamsEntity>> list2 = specificationFeign.list(specParamsDTO1);

        HashMap<Integer, String> integerStringHashMap = new HashMap<>();

        if(list2.getCode() == 200){
            List<SpecParamsEntity> paramsMy = list2.getData();
            paramsMy.stream().forEach(params ->{
                if(!StringUtils.isEmpty(params.getUnit())){
                    integerStringHashMap.put(params.getId().intValue(),params.getName() + "(" + params.getUnit() +")");
                }
                integerStringHashMap.put(params.getId().intValue(),params.getName());
            });
        }

        return integerStringHashMap;
    }
}
