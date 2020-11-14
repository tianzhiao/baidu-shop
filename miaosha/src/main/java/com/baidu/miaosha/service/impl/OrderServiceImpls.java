package com.baidu.miaosha.service.impl;

import com.baidu.miaosha.entity.Stock;
import com.baidu.miaosha.entity.StockOrder;
import com.baidu.miaosha.entity.UserEntity;
import com.baidu.miaosha.mapper.OrderMapper;
import com.baidu.miaosha.mapper.StockMapper;
import com.baidu.miaosha.mapper.UserMapper;
import com.baidu.miaosha.service.OrderService2;
import com.baidu.miaosha.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.annotation.Order;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName OrderServiceImpls
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/28
 * @Version V1.09999999999999999
 **/
@Slf4j
@Service
public class OrderServiceImpls implements OrderService2 {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private StockMapper stockMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int createPessimisticOrder(int sid){
        //校验库存(悲观锁for update)
        Stock stock = checkStockForUpdate(sid);
        //更新库存
        saleStock(stock);
        //创建订单
        int id = createOrder(stock);
        return stock.getCount() - (stock.getSale());
    }

    /**
     * 检查库存 ForUpdate
     * @param sid
     * @return
     */
    private Stock checkStockForUpdate(int sid) {
        Stock stock = stockMapper.selectByPrimaryKey(sid);
        if (stock.getCount().equals(stock.getSale())) {
            throw new RuntimeException("库存不足");
        }
        return stock;
    }

    /**
     * 更新库存
     * @param stock
     */
    private void saleStock(Stock stock) {
        stock.setSale(stock.getSale() + 1);
        stockMapper.updateByPrimaryKeySelective(stock);
    }

    /**
     * 创建订单
     * @param stock
     * @return
     */
    private int createOrder(Stock stock) {
        StockOrder order = new StockOrder();
        order.setSid(stock.getId());
        order.setName(stock.getName());
        int id = orderMapper.insertSelective(order);
        return id;
    }


    @Override
    public int createVerifiedOrder(Integer sid, Integer userId, String verifyHash) {

        // 验证是否在抢购时间内
        log.debug("请自行验证是否在抢购时间内,假设此处验证成功");

        // 验证hash值合法性
        String hashKey = CacheKey.HASH_KEY.getKey() + "_" + sid + "_" + userId;
        String verifyHashInRedis = stringRedisTemplate.opsForValue().get(hashKey);
        if (!verifyHash.equals(verifyHashInRedis)) {
            throw new RuntimeException("hash值与Redis中不符合");
        }
        log.info("验证hash值合法性成功");

        // 检查用户合法性
        UserEntity userEntity = userMapper.selectByPrimaryKey(userId.longValue());
        if (userEntity == null) {
            throw new RuntimeException("用户不存在");
        }
        log.info("用户信息验证成功：[{}]", userEntity.toString());

        // 检查商品合法性
        Stock stock = stockMapper.selectByPrimaryKey(sid);
        if (stock == null) {
            throw new RuntimeException("商品不存在");
        }
        log.info("商品信息验证成功：[{}]", stock.toString());

        //乐观锁更新库存
        saleStockOptimistic(stock);
        log.info("乐观锁更新库存成功");

        //创建订单
        createOrderWithUserInfo(stock, userId);
        log.info("创建订单成功");

        return stock.getCount() - (stock.getSale()+1);
    }


    /**
     * 创建订单
     * @param stock
     * @param userId
     */
    private void createOrderWithUserInfo(Stock stock, Integer userId) {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setCreateTime(new Date());
        stockOrder.setName(stock.getName());
        stockOrder.setUserId(userId);
        stockOrder.setSid(stock.getId());
        orderMapper.insertSelective(stockOrder);
    }



    /**
     * 更新库存 乐观锁
     * @param stock
     */
    private boolean saleStockOptimistic(Stock stock) {
        log.info("查询数据库，尝试更新库存");
        int count = stockMapper.updateStockByOptimistics(stock);
        return count != 0;
    }
}
