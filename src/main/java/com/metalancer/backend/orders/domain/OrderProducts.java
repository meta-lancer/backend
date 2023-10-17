package com.metalancer.backend.orders.domain;

import com.metalancer.backend.common.constants.ClaimStatus;
import com.metalancer.backend.common.constants.ClaimType;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.products.domain.ProductsDetail;
import lombok.Builder;
import lombok.Getter;


@Getter
public class OrderProducts {

    private ProductsDetail productsDetail;
    private String orderProductNo;
    private Integer price;
    private OrderStatus orderProductStatus;
    private ClaimType claimType;
    private ClaimStatus claimStatus;

    @Builder
    public OrderProducts(ProductsDetail productsDetail, String orderProductNo, Integer price,
        OrderStatus orderProductStatus, ClaimType claimType, ClaimStatus claimStatus) {
        this.productsDetail = productsDetail;
        this.orderProductNo = orderProductNo;
        this.price = price;
        this.orderProductStatus = orderProductStatus;
        this.claimType = claimType;
        this.claimStatus = claimStatus;
    }
}
