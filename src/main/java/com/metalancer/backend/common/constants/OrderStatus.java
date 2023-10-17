package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PAY_ING("주문중"), PAY_DONE("주문완료"), PAY_CONFIRM("구매확정"), CLAIM_REQUEST("교환/환불 접수"),
    CLAIM_ING("교환/환불 처리중"), CLAIM_DONE("교환/환불 완료");

    private final String korName;

    private OrderStatus(String korName) {
        this.korName = korName;
    }
}
