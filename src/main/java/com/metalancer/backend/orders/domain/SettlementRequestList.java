package com.metalancer.backend.orders.domain;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.common.constants.PaymentType;
import com.metalancer.backend.common.utils.Time;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SettlementRequestList {

    private final Long assetsId;
    private final String assetTitle;
    private final BigDecimal price;
    private final CurrencyType currencyType;
    private final BigDecimal chargeRate;
    private BigDecimal freelancerRate;
    private BigDecimal serviceRate;
    private final PaymentType paymentType;
    private String soldAt;

    @Builder
    public SettlementRequestList(Long assetsId, String assetTitle, BigDecimal price,
        CurrencyType currencyType,
        BigDecimal chargeRate,
        PaymentType paymentType) {
        this.assetsId = assetsId;
        this.assetTitle = assetTitle;
        this.price = price;
        this.currencyType = currencyType;
        this.chargeRate = chargeRate;
        this.paymentType = paymentType;
    }

    public void setFreelancerRate(BigDecimal freelancerRate) {
        this.freelancerRate = freelancerRate;
    }

    public void setServiceRate(BigDecimal serviceRate) {
        this.serviceRate = serviceRate;
    }

    public void setSoldAt(LocalDateTime soldAt) {
        this.soldAt = Time.convertDateToFullString(soldAt);
    }
}