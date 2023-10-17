package com.metalancer.backend.orders.domain;

import com.metalancer.backend.orders.entity.OrderProductsEntity;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatedOrder {

    private final Long ordererId;
    private final String orderNo;
    private final Integer totalPrice;
    private final String orderStatus;
    private String orderProductList;
    private final String buyerNm;
    private final String buyerPhone;
    private final String buyerEmail;

    @Builder
    public CreatedOrder(Long ordererId, String orderNo, Integer totalPrice,
        String orderStatus, String buyerNm, String buyerPhone, String buyerEmail) {
        this.ordererId = ordererId;
        this.orderNo = orderNo;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.buyerNm = buyerNm;
        this.buyerPhone = buyerPhone;
        this.buyerEmail = buyerEmail;
    }

    public void setOrderProductList(List<OrderProductsEntity> orderProductList) {
        if (orderProductList.size() > 0) {
            this.orderProductList = orderProductList.get(0).getProductsEntity().getTitle();
            if (orderProductList.size() > 1) {
                this.orderProductList += "외 " + (orderProductList.size() - 1) + "건";
            }
        }
    }
}