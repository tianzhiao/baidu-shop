package com.baidu.shop.component;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.RowBounds;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @ClassName Fence
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/11/13
 * @Version V1.09999999999999999
 **/

public class Fence implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        RowBounds rowBounds = new RowBounds();
        return null;
    }

    @Override
    public Object plugin(Object o) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }


}
