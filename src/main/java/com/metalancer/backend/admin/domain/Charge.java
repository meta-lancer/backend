package com.metalancer.backend.admin.domain;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Charge {

    private Long chargeId;
    private String chargeName;
    private BigDecimal chargeRate;

    @Builder
    public Charge(Long chargeId, String chargeName, BigDecimal chargeRate) {
        this.chargeId = chargeId;
        this.chargeName = chargeName;
        this.chargeRate = chargeRate;
    }
}