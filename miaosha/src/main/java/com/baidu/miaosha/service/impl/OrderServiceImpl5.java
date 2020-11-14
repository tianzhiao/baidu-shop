package com.baidu.miaosha.service.impl;

import com.baidu.miaosha.entity.Stock;
import com.baidu.miaosha.entity.StockOrder;
import com.baidu.miaosha.mapper.OrderMapper;
import com.baidu.miaosha.mapper.StockMapper;
import com.baidu.miaosha.service.OrderService5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName OrderServiceImpl5
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/29
 * @Version V1.09999999999999999
 **/
@Service
public class OrderServiceImpl5 implements OrderService5 {

    @Resource
    private StockMapper stockMapper;

    @Resource
    private OrderMapper orderMapper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public int createPessimisticOrder(int sid) {
        //校验库存(悲观锁for update)
        Stock stock = checkStockForUpdate(sid);
        //更新库存
        saleStock(stock);
        //创建订单
        createOrder(stock);
        return stock.getCount() - (stock.getSale());
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
        return orderMapper.insertSelective(order);
    }


    /**
     * 更新库存
     * @param stock
     */
    private void saleStock(Stock stock) {
        stock.setSale(stock.getSale() + 1);
        stockMapper.updateByPrimaryKeySelective(stock);
    }

   /* *//**
     * 更新库存 乐观锁
     * @param stock
     *//*
    private boolean saleStockOptimistic(Stock stock) {
        LOGGER.info("查询数据库，尝试更新库存");
        int count = stockService.updateStockByOptimistic(stock);
        return count != 0;
    }*/

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
}
