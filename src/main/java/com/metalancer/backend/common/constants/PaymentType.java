package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum PaymentType {

    CARD, TOSS_PAY, KAKAO_PAY, NAVER_PAY, PAYPAL, FREE;

    private PaymentType() {
    }

    public static PaymentType getType(String method, String type) {
        if ("paypal".startsWith(type)) {
            return PAYPAL;
        }
        if ("kakaopay".equals(type)) {
            return KAKAO_PAY;
        }

        if ("naverpay".equals(type)) {
            return NAVER_PAY;
        }
        if ("free".equals(type)) {
            return FREE;
        }
        // 토스페이 구분

        return CARD;
    }
}
