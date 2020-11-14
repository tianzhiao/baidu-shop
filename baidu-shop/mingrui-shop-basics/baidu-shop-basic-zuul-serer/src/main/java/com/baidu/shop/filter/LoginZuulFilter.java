package com.baidu.shop.filter;

import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.utils.CookieUtils;
import com.baidu.shop.utils.JwtUtils;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @ClassName LoginZuulFilter
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/17
 * @Version V1.09999999999999999
 **/
@Component
@Slf4j
public class LoginZuulFilter extends ZuulFilter {


    @Resource
    private JwtConfig jwtConfig;

    /**
     *
     * filter类型
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {

        //获取上下文
        RequestContext currentContext = RequestContext.getCurrentContext();

        //获取request
        HttpServletRequest request = currentContext.getRequest();

        //获取uri
        String requestURI = request.getRequestURI();

        //拦截到的uri
        log.debug("拦截的uri:{}",requestURI);

        //判断配置文件中配置的白名单是否包含 requestURI 如果包含就放过拦截 return false --》 就是filter不生效 return ture--》 就是filter生效
        //!jwtConfig.getExcludePath().contains(requestURI) && !(requestURI.indexOf("manage") != -1)
        return false;
    }

    //每秒钟向令牌桶放置100个令牌 ，限制每秒请求量最多100笔
    public static final RateLimiter RATE_LIMITER = RateLimiter.create(100);

    @Override
    public Object run() throws ZuulException {

        //获取上下文
        RequestContext currentContext = RequestContext.getCurrentContext();

        //获取request
        HttpServletRequest request = currentContext.getRequest();
        System.out.println(request.getRequestURI());
        log.debug("拦截到的uri :" + request.getRequestURI());

        //通过cookieName 获取token
        String token = CookieUtils.getCookieValue(request, jwtConfig.getCookieName());

        if(!StringUtils.isEmpty(token)){

            /**
             * 无参方法tryAcquire()的作用是尝试地获得1个许可，如果获取不到则返回false，
             * 此方法通常与if语句结合使用，其具有无阻塞的特点
             * 。无阻塞的特点可以使线程不至于在同步处一直持续等待的状态，
             * 如果if语句判断不成立则线程会继续走else语句，程序会继续向下运行。
             */
            if(RATE_LIMITER.tryAcquire()){

                try {

                    //通过token & 公钥解密 不成功就会 就会执行 catch
                    JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
                } catch (Exception e) {
                    log.debug("解密失败的token ： {}",token);
                    //响应浏览器
                    currentContext.setSendZuulResponse(false);
                    currentContext.setResponseStatusCode(HttpStatus.SC_FORBIDDEN);
                }
            }
        }else{
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(HttpStatus.SC_FORBIDDEN);
        }

        return null;
    }
}
