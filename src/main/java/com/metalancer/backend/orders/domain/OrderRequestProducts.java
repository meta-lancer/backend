package com.metalancer.backend.orders.domain;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.products.domain.RequestOption;
import com.metalancer.backend.users.domain.Creator;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;


@Getter
public class OrderRequestProducts {

    private Long orderRequestProductsId;
    private String orderProductNo;
    private RequestOption requestOption;
    private BigDecimal price;
    private CurrencyType currencyType;
    private OrderStatus orderProductStatus;
    private Creator seller;
    private String nickname;

    @Builder
    public OrderRequestProducts(Long orderRequestProductsId, String orderProductNo,
        RequestOption requestOption,
        BigDecimal price,
        CurrencyType currencyType, OrderStatus orderProductStatus, Creator seller,
        String nickname) {
        this.orderRequestProductsId = orderRequestProductsId;
        this.orderProductNo = orderProductNo;
        this.requestOption = requestOption;
        this.price = price;
        this.currencyType = currencyType;
        this.orderProductStatus = orderProductStatus;
        this.seller = seller;
        this.nickname = nickname;
    }
}
