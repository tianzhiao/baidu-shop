package com.baidu.shop.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName ObjectUtils
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/15
 * @Version V1.09999999999999999
 **/

public class ObjectUtils {

    private static Boolean isNull(Object obj){

        return obj == "" || obj == null;
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static Integer toInteger(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Double || obj instanceof Float) {
            return Integer.valueOf(StringUtils.substringBefore(obj.toString(), "."));
        }
        if (obj instanceof Number) {
            return Integer.valueOf(obj.toString());
        }
        if (obj instanceof String) {
            return Integer.valueOf(obj.toString());
        } else {
            return 0;
        }
    }

}
