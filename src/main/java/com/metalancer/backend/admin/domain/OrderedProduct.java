package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.products.domain.RequestOption;
import com.metalancer.backend.users.domain.Creator;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;


@Getter
public class OrderedProduct {

    private Creator creator;
    private Long assetsId;
    private Long orderProductsId;
    private String orderProductsNo;
    private String title;
    private OrderStatus orderStatus;
    private BigDecimal productsPrice;
    private String thumbnail;
    private RequestOption requestOption;

    @Builder
    public OrderedProduct(Creator creator, Long assetsId, Long orderProductsId,
        String orderProductsNo,
        String title, OrderStatus orderStatus, BigDecimal productsPrice,
        String thumbnail,
        RequestOption requestOption) {
        this.creator = creator;
        this.assetsId = assetsId;
        this.orderProductsId = orderProductsId;
        this.orderProductsNo = orderProductsNo;
        this.title = title;
        this.orderStatus = orderStatus;
        this.productsPrice = productsPrice;
        this.thumbnail = thumbnail;
        this.requestOption = requestOption;
    }

    public void setRequestOption(RequestOption requestOption) {
        this.requestOption = requestOption;
    }
}
