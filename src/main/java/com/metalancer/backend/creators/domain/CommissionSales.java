package com.metalancer.backend.creators.domain;

import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.products.domain.RequestOption;
import com.metalancer.backend.users.dto.UserResponseDTO.OtherCreatorBasicInfo;
import com.metalancer.backend.users.entity.User;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;


@Getter
public class CommissionSales {

    private OtherCreatorBasicInfo buyer;
    private RequestOption requestOption;
    private OrderStatus orderStatus;
    private BigDecimal productsPrice;
    private String purchasedAt;
    private String payConfirmedAt;
    private String claimAt;

    @Builder
    public CommissionSales(User buyer, RequestOption requestOption, OrderStatus orderStatus,
        BigDecimal productsPrice, String purchasedAt, String payConfirmedAt) {
        this.buyer = buyer.toOtherCreatorBasicInfo();
        this.requestOption = requestOption;
        this.orderStatus = orderStatus;
        this.productsPrice = productsPrice;
        this.purchasedAt = purchasedAt;
        this.payConfirmedAt = payConfirmedAt;
    }

    public void setClaimAt(String claimAt) {
        this.claimAt = claimAt;
    }
}
