package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PAY_ING("주문중"), PAY_DONE("진행중"), PAY_CONFIRM("구매확정"), CANCEL_DONE("주문 취소"),
    CLAIM_DONE("취소/문제해결"), CLAIM_ING("분쟁 조정 중");

    private final String korName;

    private OrderStatus(String korName) {
        this.korName = korName;
    }
}
