package com.baidu.shop.web;

import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpecParamsEntity;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.service.GoodsServiceI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PageController
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/23
 * @Version V1.09999999999999999
 **/
@Controller
@RequestMapping("item/")
public class PageController {


    @Resource
    private GoodsServiceI goodsService;

    @GetMapping("/{spuId}.html")
    public String test(@PathVariable("spuId") Integer spuId, ModelMap modelMap){

        Map<String ,Object> map = goodsService.getGoodsBySpuId(spuId);

        modelMap.addAttribute("spuInfo",(SpuDTO) map.get("spuInfo"));
        modelMap.addAttribute("brandInfo2",(BrandEntity) map.get("brandInfo2"));
        modelMap.addAttribute("groupList",(List<SpecGroupEntity>) map.get("groupList"));
        modelMap.addAttribute("paramsMap",(HashMap<Integer, String>) map.get("paramsMap"));
        modelMap.addAttribute("spuDetail",(SpuDetailEntity) map.get("spuDetail"));
        modelMap.addAttribute("skus",(List<SkuDTO>) map.get("skus"));
        modelMap.addAttribute("paramsMy",(HashMap<Integer, String>) map.get("paramsMy"));
        modelMap.addAttribute("paramsAndGroup",(List<SpecGroupDTO>) map.get("paramsAndGroup"));

        return "item";
    }
}
