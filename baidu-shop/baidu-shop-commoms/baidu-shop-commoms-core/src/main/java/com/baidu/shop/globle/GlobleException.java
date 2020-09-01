package com.baidu.shop.globle;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.status.HTTPStatus;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GlobleException
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/31
 * @Version V1.09999999999999999
 **/
@RestControllerAdvice
@Slf4j
public class GlobleException {

    //运行时异常 RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public Result<JSONObject> glebleError(Exception e, HttpServletRequest request){

        Result<JSONObject> objectResult = new Result<>();

        objectResult.setCode(HTTPStatus.ERROR);

        objectResult.setMessage(e.getMessage());

        log.debug(e.getMessage());

        return objectResult;
    }


    //方法参数无效异常 MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  List<Result<JsonObject>> methodArgumentNotValidHandler(HttpServletRequest request,
                                              MethodArgumentNotValidException exception){

        List<Result<JsonObject>> objects = new ArrayList<>();

        //返回的错误信息

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {

            Result<JsonObject> jsonObjectResult = new Result<>();

            jsonObjectResult.setCode(HTTPStatus.PARAMS_VALIDATE_ERROR);

            jsonObjectResult.setMessage("Field --> " + error.getField() + " : " + error.getDefaultMessage());

            log.debug("Field --> " + error.getField() + " : " + error.getDefaultMessage());
            objects.add(jsonObjectResult);
        }

        return objects;

    }

}
