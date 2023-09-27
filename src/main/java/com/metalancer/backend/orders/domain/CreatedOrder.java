package com.metalancer.backend.orders.domain;

import com.metalancer.backend.orders.entity.OrderProductsEntity;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatedOrder {

    private Long ordererId;
    private String orderNo;
    private Integer totalPrice;
    private String orderStatus;
    private String orderProductList;

    @Builder
    public CreatedOrder(Long ordererId, String orderNo, Integer totalPrice,
        String orderStatus) {
        this.ordererId = ordererId;
        this.orderNo = orderNo;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
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