package com.baidu.shop.service.impl;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.constant.CarConstGoods;
import com.baidu.shop.dto.Car;
import com.baidu.shop.dto.SpecParamsDTO;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.SkuEntity;
import com.baidu.shop.entity.SpecParamsEntity;
import com.baidu.shop.feign.GoodsFeign;
import com.baidu.shop.feign.SpecParamFeign;
import com.baidu.shop.reids.repository.RedisRepository;
import com.baidu.shop.service.CarService;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utlis.JSONUtil;
import com.baidu.shop.utlis.ObjectUtil;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName CarService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/19
 * @Version V1.09999999999999999
 **/
@Slf4j
@RestController
public class CarServiceImpl extends BeanApiService implements CarService {

    /**
     * jwt 配置
     */
    @Resource
    private JwtConfig jwtConfig;



    /**
     * goods 通过 feign调用
     */
    @Resource
    private GoodsFeign goodsFeign;

    /**
     * redis 工具
     */
    @Resource
    private RedisRepository redisRepository;


    /**
     * param feign
     */
    @Resource
    private SpecParamFeign specParamFeign;
    /**
     * 新增购物车
     * @param car
     * @return
     */

    @Override
    public Result<JSONObject> addCar(Car car,String token) {

        Car addCar = null;

        try {
            UserInfo infoFromToken = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());

            Car carItem = redisRepository.getHash(CarConstGoods.GOODS_CAR_PRE + infoFromToken.getId(), car.getSkuId().toString(), Car.class);
            log.debug("从redis种获取的 : {}", carItem);
            if(ObjectUtil.isObj(carItem)){

                Result<SkuEntity> resultSku = goodsFeign.getBySkuId(car.getSkuId());

                if(resultSku.getCode() == 200){
                    SkuEntity sku = resultSku.getData();

                    car.setSkuId(sku.getId());
                    car.setImage(StringUtils.isEmpty(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
                    HashMap<String, String> sowSpecMap = new HashMap<>();
                    Map<String, String> nowSpec = JSONUtil.toMapValueString(sku.getOwnSpec());
                    nowSpec.forEach((k,v) -> {

                        SpecParamsDTO specParamsDTO = new SpecParamsDTO();
                        specParamsDTO.setId(Long.valueOf(k));

                        Result<List<SpecParamsEntity>> paramResult = specParamFeign.list(specParamsDTO);
                        if (paramResult.getCode() == 200) {
                            List<SpecParamsEntity> paramsEntities = paramResult.getData();
                            paramsEntities.stream().forEach(param -> {
                                sowSpecMap.put(param.getName(),v);
                            });
                        }
                    });

                    car.setOwnSpec(JSONUtil.toJsonString(sowSpecMap));
                    car.setPrice(sku.getPrice().longValue());
                    car.setTitle(sku.getTitle());
                    addCar = car;
                    //redis 中的hash数据存储结构为 Map<String,Map<String,String>>
                    //key : userId value分为 key:skuId value: car

                }

                //redisRepository.setHash(GOODS_CAR_PRE + infoFromToken.getId(),car.getSkuId().toString(), JSONUtil.toJsonString(addCar));
            }else{
                carItem.setNum(car.getNum() +carItem.getNum());
                addCar = carItem;
                log.debug("合并num为 : {}",carItem.getNum());
            }
            redisRepository.setHash(CarConstGoods.GOODS_CAR_PRE + infoFromToken.getId(),car.getSkuId().toString(), JSONUtil.toJsonString(addCar));
            log.debug("添加到redis中成功 key: {}",CarConstGoods.GOODS_CAR_PRE + infoFromToken.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> mergeCar(String clientCarList,String token) {
        //clientCarList（json类型的字符串） 转换成 对象
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(clientCarList);
        List<Car> carList = com.alibaba.fastjson.JSONObject.parseArray(jsonObject.get("clientCarList").toString(), Car.class);

        carList.stream().forEach(car -> this.addCar(car,token));

        return this.setResultSuccess();
    }

    @Override
    public Result<List<Car>> getCar(String token) {

        List<Car> resultCarList = new ArrayList<>();
        try {
            UserInfo infoFromToken = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());

            Map<String, String> hash = redisRepository.getHash(CarConstGoods.GOODS_CAR_PRE + infoFromToken.getId());
            hash.forEach((key,value) -> {

                resultCarList.add(JSONUtil.toBean(value, Car.class));
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.setResultSuccess(resultCarList);
    }


    @Override
    public Result<JSONObject> numUpdate(String token, Long skuId, Integer type) {

        try {
            UserInfo infoFromToken = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());

            Car redisCar = redisRepository.getHash(CarConstGoods.GOODS_CAR_PRE + infoFromToken.getId(), skuId.toString(), Car.class);

            redisCar.setNum(type == 1 ? redisCar.getNum() -1 : redisCar.getNum() + 1);

            redisRepository.setHash(CarConstGoods.GOODS_CAR_PRE + infoFromToken.getId(), skuId.toString(),JSONUtil.toJsonString(redisCar));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.setResultSuccess();
    }

    @Override
    public Result<List<Car>> delGoodsCar(String token, String skuId) {

        List<Car> carList = new ArrayList<>();

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            if(ObjectUtil.isObj(skuId)) throw new RuntimeException("skuID不能为空");

            Arrays.asList(skuId.split(",")).stream().forEach(skuIdStr -> {

                Car car = redisRepository.getHash(CarConstGoods.GOODS_CAR_PRE + userInfo.getId(), skuIdStr, Car.class);

                if(!ObjectUtil.isObj(car)){
                    redisRepository.setHash(CarConstGoods.CAR_DELETE_PRE + userInfo.getId(),car.getSkuId().toString(),JSONUtil.toJsonString(car));
                    Car delCar = redisRepository.getHash(CarConstGoods.CAR_DELETE_PRE + userInfo.getId(), car.getSkuId().toString(), Car.class);
                    carList.add(delCar);
                    redisRepository.delHash(CarConstGoods.GOODS_CAR_PRE + userInfo.getId(),skuIdStr);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.setResultSuccess(carList);
    }
}
