package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum HotPickType {

    NEW("신규에셋"),
    SALE("세일"),
    FREE("무료 인기에셋"),
    CHARGE("유료 인기에셋");

    private final String korName;

    private HotPickType(String korName) {
        this.korName = korName;
    }
}
