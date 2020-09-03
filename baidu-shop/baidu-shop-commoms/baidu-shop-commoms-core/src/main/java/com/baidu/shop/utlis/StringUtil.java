package com.baidu.shop.utlis;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * @ClassName StringUtil
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/8/31
 * @Version V1.09999999999999999
 **/

public class StringUtil {


    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static Integer paramsInteger(String str){

        return Integer.parseInt(str);
    }

    public static Boolean isIntNotNull(Integer id){

        return null != id && 0 != id;
    }




}
