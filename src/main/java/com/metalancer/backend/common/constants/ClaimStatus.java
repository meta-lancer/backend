package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum ClaimStatus {
    REQUEST("접수"), ING("처리중"), APPROVE("승인"),
    REJECT("반려");

    private final String korName;

    private ClaimStatus(String korName) {
        this.korName = korName;
    }
}
