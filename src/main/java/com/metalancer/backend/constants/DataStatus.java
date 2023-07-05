package com.metalancer.backend.constants;

import lombok.Getter;

@Getter
public enum DataStatus {

    ACTIVE("존재"),
    DELETED("삭제"),
    BANNED("금지"),
    PENDING("대기중");

    private final String korName;

    private DataStatus(String korName) {
        this.korName = korName;
    }
}
