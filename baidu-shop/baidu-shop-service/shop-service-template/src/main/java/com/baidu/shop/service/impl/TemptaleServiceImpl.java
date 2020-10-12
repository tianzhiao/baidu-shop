package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.*;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpecParamsEntity;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.feign.BrandFeign;
import com.baidu.shop.feign.SearchFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.service.GoodsServiceI;
import com.baidu.shop.service.TemplateService;
import com.baidu.shop.utlis.BaiduBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName TemptaleServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/25
 * @Version V1.09999999999999999
 **/
@RestController
public class TemptaleServiceImpl extends BeanApiService implements TemplateService {


    @Resource
    private SearchFeign searchFeign;

    @Value("${mrshop.static.html.path}")
    private String staticHTMLPath;
    //注入静态化模版
    @Autowired
    private TemplateEngine templateEngine;

    @Resource
    private GoodsServiceI goodsService;

    @Override
    public Result<JSONObject> createStaticHTMLTemplate(Integer spuId) {
        Map<String, Object> goodsBySpuId = goodsService.getGoodsBySpuId(spuId);
        Context context = new Context();
        context.setVariables(goodsBySpuId);
        File file = new File(staticHTMLPath, spuId + ".html");
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file,"UTF-8");
            templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            printWriter.close();
        }
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> initStaticHTMLTemplate() {
        SpuDTO spuDTO = new SpuDTO();
        Result<List<SpuDTO>> listResult = searchFeign.list2(spuDTO);

        if (listResult.getCode() == 200) {
            listResult.getData().stream().forEach(spu ->{
                this.createStaticHTMLTemplate(spu.getId());
            });
        }
        return this.setResultSuccess();
    }

}
