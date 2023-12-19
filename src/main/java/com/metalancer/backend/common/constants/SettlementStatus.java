package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum SettlementStatus {
    REQUEST("정산 요청"), ING("정산 예정"), COMPLETE("정산 완료"),
    REJECT("정산 반려");

    private final String korName;

    private SettlementStatus(String korName) {
        this.korName = korName;
    }
}
