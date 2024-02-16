package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.common.constants.PaymentType;
import com.metalancer.backend.users.entity.UserDomain;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;


@Getter
public class UserCompletedOrder {

    private UserDomain user;
    private Long orderId;
    private String orderNo;
    private Integer boughtCnt;
    private OrderStatus orderStatus;
    private CurrencyType currencyType;
    private PaymentType payMethod;
    private BigDecimal price;
    private BigDecimal totalChecksum;
    private String purchasedAt;
    private List<OrderedProduct> orderedProductList;

    @Builder
    public UserCompletedOrder(UserDomain user, Long orderId, String orderNo,
        OrderStatus orderStatus, CurrencyType currencyType, PaymentType payMethod, BigDecimal price,
        BigDecimal totalChecksum, String purchasedAt) {
        this.user = user;
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.currencyType = currencyType;
        this.payMethod = payMethod;
        this.price = price;
        this.totalChecksum = totalChecksum;
        this.purchasedAt = purchasedAt;
    }

    public void setBoughtCnt(Integer boughtCnt) {
        this.boughtCnt = boughtCnt;
    }

    public void setOrderedProductList(
        List<OrderedProduct> orderedProductList) {
        this.orderedProductList = orderedProductList;
    }
}
