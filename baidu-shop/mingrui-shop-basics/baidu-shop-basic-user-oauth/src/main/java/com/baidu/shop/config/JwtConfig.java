package com.baidu.shop.config;

import com.baidu.shop.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @ClassName JwtConfig
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/15
 * @Version V1.09999999999999999
 **/
@Configuration
@Data
@Slf4j
public class JwtConfig {

    /**
     * 密钥
     */
    @Value("${mrshop.jwt.secret}")
    private String secret;

    /**
     * token 有效时间，分钟为单位
     */
    @Value("${mrshop.jwt.expire}")
    private Integer expire;

    /**
     *cookie名字
     */
    @Value("${mrshop.jwt.cookieName}")
    private String cookieName;

    /**
     *cookie 有效时间，秒为单位
     */
    @Value("${mrshop.jwt.cookieMaxAge}")
    private Integer cookieMaxAge;

    /**
     * 公钥路径
     */
    @Value("${mrshop.jwt.pubKeyPath}")
    private String pubKeyPath;

    /**
     * 私钥路径
     */
    @Value("${mrshop.jwt.priKeyPath}")
    private String priKeyPath;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 私钥
     */
    private PrivateKey privateKey;


    /**
     * construct
     */
    @PostConstruct
    public void init(){

        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            if(!pubKey.exists() || !priKey.exists()) {
                //生成公钥和私钥
                RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
            }

            /**
             * 获取公钥和私钥
             */
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {

            log.error("初始化公钥和私钥失败：{}",e.getMessage());
            e.printStackTrace();
        }
    }

}
