package com.baidu.miaosha.service.impl;

import com.baidu.miaosha.entity.StockOrder;
import com.baidu.miaosha.entity.Stock;
import com.baidu.miaosha.mapper.OrderMapper;
import com.baidu.miaosha.mapper.StockMapper;
import com.baidu.miaosha.service.OrderService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName OrderServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/27
 * @Version V1.09999999999999999
 **/
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    public int createWrongOrder(int sid) {
        return 0;
    }

    @Override
    public int createOptimisticOrder(int sid) {
        return 0;
    }

    RateLimiter rateLimiter = RateLimiter.create(10);

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private StockMapper stockMapper;



    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
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
}
