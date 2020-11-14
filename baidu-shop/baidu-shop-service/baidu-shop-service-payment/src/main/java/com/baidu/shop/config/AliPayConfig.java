package com.baidu.shop.config;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @ClassName AliPayConfig
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/22
 * @Version V1.09999999999999999
 **/
@Component
public class AliPayConfig {

    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016102600766461";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCuWnzG/DpHWLuKm1nTmHv3QiXuEodlNVQqi/Wax5gxGGD+r0RiRPiG8It5l8L4nFyE6j3VIUas4HBiL27ED8QoLwZ4gA4X5GM6hy5UBtLVHPqvYzt7vbyl3Ea0JeGjFAYww3yO9ptVfJj5HrHXsazcYr5zlNVV3EIwlVsdGnLvus//+LPwd+FkKcAB2FZpR5gi35mp15MOEVfx4P7X9z0Ew/N+255y8Qr22OrP+gCpaNQfxYWAwltkJVfd6uE/RV0Gz64EgFttnDiW2RI/tG87HnaBmpl5nbp7OGHgShA/4FBha4ei5k5tqy00qaW7/CMR77Jx2xdlycLDEZ88pkbvAgMBAAECggEBAIFUQ7xCemRT8IEnLSFkuYe2fHY1LlOnlDAfM2bFaSH5yHECx0HxkTxfD8Z5CTnqmkJJkuYWUduIfDSIpKwwWbvI7g7SRiH42pYekgHoMdm9LkKM5CGso1OQqq2Ub+PMxa7EQw+045Is4zffSeIKPIQZhtYrb3fJ8xsgnr5e0ST6/zzM+OqH5R86RIIERkLZat1pOn5aaVfFOK1QDTbTtXcHt8q3XOmG9433cimpykEAGQ1fPXZ97La20jjWAJ40/q5yvpUCUAC3806Bi9lPC7O4Cg6DSPdgmVyGwyFrkcZXE665Sq7ZUCqHuhcPjp2XOszguSJh8C++C7ufXtWxHCECgYEA1gNAtxMZvVafysNqaIBoxYwxPx+C2/yBNSYzyxqq0TcmPuZrEVcLHSbjq4XqFte/CorG1svQT/PQmD1JvNdHJwy+OvAJyrzSxzOyPpGdlL7s/rARGfizoDW+BkVYMJz2ZQPcCmIY8AaLNokLmTpLK3YYgSdVQ/Z9ecF1NtSpljECgYEA0I9bXTiHpQdzAoHzxp5niW7f0C55pHkeF2A9SjMuHliEvBDU/OSFS8JzU7n0wwpp968fpMrNjg+BjX2sCmVcrKVIMg+ZDCSlbDwT/68l95DPsaMdY15hvSPdKW8wxqDUQ+D5xxfn5jHf83mYw28GGhXQ6lHPhY/HJ/lSVS3txx8CgYANVUiL31nELQhphoEzn4RnglNzHlB1yiZ2nDmv1qp7vFbaplpXSzf7Cfg3MHGkGK7jRkAbE7mXsZX2ViMo67SYHzgezeOeSONM29CoC0RpBvgW6fs2xgLS4WZEV7+lz7GeG35y+yeYlJ/JtwIM+s/CFmpyaXPrdwjcNAxMbTVfkQKBgEC7Tfs4EXnaPwsXNIeAINiaLiNaWW0AHqPYvg0qp3TJuqD4hHLdprWl68+f1uU2dT9n/mVSgMxH6cj1qZEsdsArKKZJCxKmySU33GkufpVBQAV9gCCYabOfzwQOO9BamKdcy4nM1LAw6LaUFiMKcZVlEgu4SrXEs2anzrMbZ0K3AoGAEoc6eG2f2B5lWHhbbLIwBe3xxWUF3lXNGWM7wVEEaV/DbIVr8/jgAnUgzZaIj9Vjjz4NQ0s9Db20dXTcu3/b92BtU6DyDwzlMbC/xWc00m+R7esfDssNV7L+973jxOBrhNpv4VeQZYt4CoQqX2FSQpdSHMqqdLEt0jWX9H1PjZk=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjQvq9MrgRUEBHwaiVPDJdF28l9GmrsCa/Aj+s6sUeAvwf2jRpWNBp2kYq+Q1Jbx1Hep/26yD59NNEYomiFxiCWbLY0FzY7bx6xDJTWhTOkxOTapg/HC1Xd2AvPKShKHbnvT2BRArupQqdxOIQ4UYOQ0jI+4CnajL48sXPaAS/w1L0WCKo+ylKXQnSiMqTrty7cOP/am4QZM/SpT1YRFUJRiUsWAMHWY7jGJ8VSz338Wr1mexgsbmt0/iVJS7PV3BTExjVcKfo/FtxLRIt/420YDeKGARNKQTh8FRoVd3t2tukeYG1NMVg/otVTNvZaCwhpsfy0kN9Iku/QT0eSTltQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8900/payment/returnNotify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8900/payment/returnUrl";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "D:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
