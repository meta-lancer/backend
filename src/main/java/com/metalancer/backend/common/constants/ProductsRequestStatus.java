package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum ProductsRequestStatus {

    Want("제작요청"),
    Wait("진행중"),
    Done("진행완료"),
    Make("제작홍보");

    private final String korName;

    private ProductsRequestStatus(String korName) {
        this.korName = korName;
    }
}
