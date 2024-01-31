package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum PaymentInfoType {
    PERSONAL, CORPORATION;

    private PaymentInfoType() {
    }
}
