package com.baidu.shop.service.impl;

import com.baidu.shop.IdWorker;
import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.business.service.OrderService;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.constant.CarConstGoods;
import com.baidu.shop.dto.Car;
import com.baidu.shop.dto.OrderDTO;
import com.baidu.shop.dto.OrderInfo;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.AddrEntity;
import com.baidu.shop.entity.OrderDetailEntity;
import com.baidu.shop.entity.OrderEntity;
import com.baidu.shop.entity.OrderStatusEntity;
import com.baidu.shop.feign.AddrFeign;
import com.baidu.shop.feign.StockFeign;
import com.baidu.shop.mapper.OrderDetailMapper;
import com.baidu.shop.mapper.OrderMapper;
import com.baidu.shop.mapper.OrderStatusMapper;
import com.baidu.shop.redis.repository.RedisRepository;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utlis.BaiduBeanUtil;
import com.baidu.shop.utlis.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName OrderServiceImpl
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/21
 * @Version V1.09999999999999999
 **/
@Slf4j
@RestController
public class OrderServiceImpl extends BeanApiService implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderStatusMapper orderStatusMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Resource
    private IdWorker idWorker;

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private StockFeign stockFeign;

    @Resource
    private AddrFeign addrFeign;


    @Transactional
    @Override
    public Result<String> createOrder(OrderDTO orderDTO,String token) {
        long orderId = idWorker.nextId();

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());

            OrderEntity orderEntity = new OrderEntity();

            orderEntity.setPaymentType(orderDTO.getPayType());
            orderEntity.setOrderId(orderId);

            Date date = new Date();
            orderEntity.setCreateTime(date);
            orderEntity.setUserId(userInfo.getId().toString());

            //买家是否留言
            orderEntity.setBuyerRate(0);
            orderEntity.setBuyerMessage("真垃圾啊");
            orderEntity.setBuyerNick(userInfo.getUsername());
            //发票类型(0无发票1普通发票，2电子发票，3增值税发票)
            orderEntity.setInvoiceType(1);
            //订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
            orderEntity.setSourceType(1);

            String skuIdArr = orderDTO.getSkuId();

            //总价
            List<Long> totalPrice = Arrays.asList(0L);


            List<OrderDetailEntity> detailEntityList = Arrays.asList(skuIdArr.split(",")).stream().map(skuId -> {
                Car redisCar = redisRepository.getHash(CarConstGoods.GOODS_CAR_PRE + userInfo.getId(), skuId, Car.class);

                if(ObjectUtil.isObj(redisCar)) throw new RuntimeException("数据出现问题");
                OrderDetailEntity orderDetailEntity = new OrderDetailEntity();

                orderDetailEntity.setImage(redisCar.getImage());
                orderDetailEntity.setOrderId(orderId);
                orderDetailEntity.setNum(redisCar.getNum());
                orderDetailEntity.setOwnSpec(redisCar.getOwnSpec());
                orderDetailEntity.setPrice(redisCar.getPrice());
                orderDetailEntity.setSkuId(Long.valueOf(skuId));
                orderDetailEntity.setTitle(redisCar.getTitle());

                totalPrice.set(0, redisCar.getNum() * redisCar.getPrice());

                //库存操作
                stockFeign.edit(Long.valueOf(skuId),redisCar.getNum().longValue());

                return orderDetailEntity;
            }).collect(Collectors.toList());

            //总价
            orderEntity.setActualPay(totalPrice.get(0));
            orderEntity.setTotalPay(totalPrice.get(0));

            //订单状态
            OrderStatusEntity orderStatusEntity = new OrderStatusEntity();

            orderStatusEntity.setCreateTime(date);
            orderStatusEntity.setOrderId(orderId);
            //状态：1、未付款 2、已付款,未发货 3、已发货,未确认 4、交易成功 5、交易关闭 6、已评价
            orderStatusEntity.setStatus(1);


            Result<AddrEntity> addrById = addrFeign.getAddrById(orderDTO.getAddrId().longValue());
            if(addrById.getCode() == 200){
                AddrEntity data = addrById.getData();
                orderEntity.setReceiver(data.getName());

                orderEntity.setReceiveCity(data.getCity() + "");

                orderEntity.setReceiverAddress(data.getAddress());
                orderEntity.setReceiverDistrict(data.getDistrict());
                orderEntity.setReceiverZip(data.getZipCode());
                orderEntity.setReceiverState(data.getState());
            }
            //入库
            orderMapper.insertSelective(orderEntity);

            orderDetailMapper.insertList(detailEntityList);

            orderStatusMapper.insertSelective(orderStatusEntity);




            //删除redis中的数据
            Arrays.asList(skuIdArr.split(",")).stream().forEach(skuId -> {
                boolean b = redisRepository.delHash(CarConstGoods.GOODS_CAR_PRE + userInfo.getId(), skuId);
                log.debug("redis 删除的数据 skuId: {}, b : {}",skuId,b ? "成功" : "失败");
            });
            //将mysql中的库存数据更新


        } catch (Exception e) {
            e.printStackTrace();
        }


        return this.setResult(HttpStatus.SC_OK,"",orderId + "");
    }


    @Override
    public Result<OrderInfo> getOrderByOrderId(Long orderId) {

        OrderEntity orderEntity = orderMapper.selectByPrimaryKey(orderId);

        OrderInfo orderInfo = BaiduBeanUtil.copyProperties(orderEntity, OrderInfo.class);

        OrderStatusEntity orderStatusEntity = orderStatusMapper.selectByPrimaryKey(orderId);

        orderInfo.setOrderStatusEntity(orderStatusEntity);

        Example example = new Example(OrderDetailEntity.class);
        example.createCriteria().andEqualTo("orderId",orderId);
        List<OrderDetailEntity> detailEntityList = orderDetailMapper.selectByExample(example);
        orderInfo.setOrderDetailList(detailEntityList);

        return this.setResultSuccess(orderInfo);
    }

    @Override
    public Result<JSONObject> editStatus(String orderId) {
        Example example = new Example(OrderStatusEntity.class);
        example.createCriteria().andEqualTo("orderId",Long.valueOf(orderId));
        List<OrderStatusEntity> orderStatusEntities = orderStatusMapper.selectByExample(example);
        orderStatusEntities.stream().forEach(status -> {
            OrderStatusEntity orderStatusEntity = new OrderStatusEntity();
            orderStatusEntity.setStatus(2);
            orderStatusEntity.setPaymentTime(status.getCreateTime());
            orderStatusEntity.setOrderId(Long.valueOf(orderId));
            orderStatusMapper.updateByPrimaryKeySelective(orderStatusEntity);
        });
        return this.setResultSuccess();
    }
}
