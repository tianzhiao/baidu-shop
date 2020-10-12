package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName TemplateService
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/25
 * @Version V1.09999999999999999
 **/
@Api(tags = "这是创建模板接口")
public interface TemplateService {

    @ApiOperation(value = "创建静态的html模板")
    @GetMapping("/template/createStaticHTMLTemplate")
    Result<JSONObject> createStaticHTMLTemplate(Integer spuId);

    @ApiOperation(value = "初始化一个模板")
    @GetMapping(value = "template/initStaticHTMLTemplate")
    Result<JSONObject> initStaticHTMLTemplate();
}
    