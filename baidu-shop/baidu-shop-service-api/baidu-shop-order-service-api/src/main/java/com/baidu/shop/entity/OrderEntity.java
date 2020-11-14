package com.baidu.shop.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName OrderEntity
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/21
 * @Version V1.09999999999999999
 **/
@Data
@ApiModel(value = "订单model")
@Table(name = "tb_order")
public class OrderEntity {

    @Id
    private Long orderId;//订单id

    private Long totalPay;//总金额,单位为分

    private Long actualPay;//实付金额,有活动或者优惠的话可能会与实际金额不一直

    private String promotionIds;//促销/活动的id集合

    private Integer paymentType;//支付类型

    private Date createTime;//订单生成时间

    private String userId;//用户id,可以换成与用户表一直的数据类型

    private String buyerMessage;//买家留言

    private String buyerNick;

    private Integer buyerRate;//买家是否已经评价

    private Integer invoiceType;//发票类型;

    private Integer sourceType;//订单来源

    //省
    private String receiveCity;

    //市
    private String receiverState;

    //区
    private String receiverDistrict;

    private String receiverAddress;

    private String receiverMobile;

    private String receiverZip;

    private String receiver;

}
