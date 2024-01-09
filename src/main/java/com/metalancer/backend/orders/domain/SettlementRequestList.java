package com.metalancer.backend.orders.domain;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.common.constants.PaymentType;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SettlementRequestList {

    private final Long assetsId;
    private final String assetTitle;
    private final BigDecimal price;
    private final CurrencyType currencyType;
    private final BigDecimal chargeRate;
    private final BigDecimal freelancerRate;
    private final BigDecimal serviceRate;
    private final PaymentType paymentType;
    private final String soldAt;
    
    @Builder
    public SettlementRequestList(Long assetsId, String assetTitle, BigDecimal price,
        CurrencyType currencyType,
        BigDecimal chargeRate, BigDecimal freelancerRate, BigDecimal serviceRate,
        PaymentType paymentType, String soldAt) {
        this.assetsId = assetsId;
        this.assetTitle = assetTitle;
        this.price = price;
        this.currencyType = currencyType;
        this.chargeRate = chargeRate;
        this.freelancerRate = freelancerRate;
        this.serviceRate = serviceRate;
        this.paymentType = paymentType;
        this.soldAt = soldAt;
    }
}