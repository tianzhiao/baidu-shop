import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utils.RsaUtils;
import org.junit.*;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @ClassName JwtTest
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/15
 * @Version V1.09999999999999999
 **/

public class JwtTest {

    //公钥位置
    private static final String pubKeyPath = "T:\\severe\\rea.pub";
    //私钥位置
    private static final String priKeyPath = "T:\\severe\\rea.pri";
    //公钥对象
    private PublicKey publicKey;
    //私钥对象
    private PrivateKey privateKey;


    /**
     * 生成公钥私钥 根据密文
     * @throws Exception
     */
    @Test
    public void genRsaKey() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "mingrui");
    }


    /**
     * 从文件中读取公钥私钥
     * @throws Exception
     */
    @Before
    public void getKeyByRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    /**
     * 根据用户信息结合私钥生成token
     * @throws Exception
     */
    @Test
    public void genToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(2, "asd"), privateKey, 2);
        System.out.println("user-token = " + token);
    }


    /**
     * 结合公钥解析token
     * @throws Exception
     */
    @Test
    public void parseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MiwidXNlcm5hbWUiOiJhc2QiLCJleHAiOjE2MDI3NDExOTV9.B32DfQ1zif90Eu4Rz-uiD50HKu5x1SPtYbP3BUKowGf9RWK4pKOesJlAFK4X6aUwSnoj83GwqEAruv3-1vrA8zYz6LjFNeN05LIhRKlFcXRKLOxX7GtNKe8iWmS8eX8VX_s0Z9lTtYMncp9g-HYjPMN_paVlEO1sg-VzJ_laXic";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }

}
