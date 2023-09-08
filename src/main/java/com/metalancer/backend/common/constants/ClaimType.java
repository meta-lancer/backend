package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum ClaimType {
    REFUND("환불"), EXCHANGE("교환");

    private final String korName;

    private ClaimType(String korName) {
        this.korName = korName;
    }
}
