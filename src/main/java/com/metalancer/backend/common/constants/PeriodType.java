package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum PeriodType {

    WEEKLY("주간"),
    MONTHLY("월간"),
    YEARLY("연간"),
    SIX_MONTHS("6개월간");

    private final String korName;

    private PeriodType(String korName) {
        this.korName = korName;
    }
}
