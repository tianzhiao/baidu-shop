package com.baidu.shop.base;


import lombok.NoArgsConstructor;

/**
 * @ClassName Result
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/27
 * @Version V1.09999999999999999
 **/
public class Result<T> {

    private Integer code;//返回码

    private String message;//返回消息

    private T data;//返回数据

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = (T) data;
    }

    public Result() {
        super();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
