package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.common.constants.PaymentType;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class AdminSettlementProducts {

    private Long assetsId;
    private String assetTitle;
    private BigDecimal price;
    private CurrencyType currencyType;
    private BigDecimal chargeRate;
    private BigDecimal freelancerRate;
    private BigDecimal serviceRate;
    private PaymentType paymentType;
    private boolean hasSettled;
    private String soldAt;
}