package com.baidu.shop.service.impl;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.dto.AddrDTO;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.AddrEntity;
import com.baidu.shop.mapper.AddrMapper;
import com.baidu.shop.service.AddrService;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utlis.BaiduBeanUtil;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

/**
 * @ClassName AddrServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/23
 * @Version V1.09999999999999999
 **/

@RestController
public class AddrServiceImpl extends BeanApiService implements AddrService {

    @Resource
    private AddrMapper addrMapper;

    @Resource
    private JwtConfig jwtConfig;

    @Override
    public Result<List<AddrEntity>> getAddrInfo(String token) {
        List<AddrEntity> addrEntities = null;
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            Example example = new Example(AddrEntity.class);
            example.createCriteria().andEqualTo("userId",userInfo.getId());
            addrEntities = addrMapper.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.setResultSuccess(addrEntities);
    }

    @Override
    public Result<JSONObject> saveAddr(String token, AddrDTO addrDTO) {

        AddrEntity addrEntity = BaiduBeanUtil.copyProperties(addrDTO, AddrEntity.class);

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            addrEntity.setUserId(userInfo.getId().longValue());
            addrMapper.insertSelective(addrEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> updateAddr(AddrDTO addrDTO) {

        addrMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(addrDTO,AddrEntity.class));

        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> deleteAddr(Long id) {
        HashSet<Object> objects = new HashSet<>();
        addrMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess()  ;
    }


    @Override
    public Result<AddrEntity> getAddrById(Long id) {
        AddrEntity addrEntity = addrMapper.selectByPrimaryKey(id);
        return this.setResultSuccess(addrEntity);
    }
}
