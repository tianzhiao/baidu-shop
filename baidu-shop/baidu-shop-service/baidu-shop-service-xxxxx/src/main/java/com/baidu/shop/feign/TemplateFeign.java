package com.baidu.shop.feign;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.service.TemplateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName TemptaleFeign
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/26
 * @Version V1.09999999999999999
 **/
@FeignClient(value = "template-server",contextId = "TemplateService")
public interface TemplateFeign{


    @ApiOperation(value = "创建静态的html模板")
    @GetMapping("/template/createStaticHTMLTemplate")
    Result<JSONObject> createStaticHTMLTemplate(@RequestParam Integer spuId);

    @ApiOperation(value = "初始化一个模板")
    @GetMapping(value = "template/initStaticHTMLTemplate")
    Result<JSONObject> initStaticHTMLTemplate();

}
