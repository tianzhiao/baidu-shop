package com.baidu.shop.utlis;

import java.util.List;

/**
 * @ClassName ObjectUil
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/3
 * @Version V1.09999999999999999
 **/

public class ObjectUtil {

    public static Boolean isNotEmpty(List<?> list){

        return list != null && list.size() != 0;
    }
}