package com.metalancer.backend.creators.domain;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DaySalesReport {

    private final String day;
    private final int totalPrice;
    private final BigDecimal totalPriceUSD;
    private final int salesCnt;

    @Builder
    public DaySalesReport(String day, Integer totalPrice, BigDecimal totalPriceUSD, int salesCnt) {
        this.day = day;
        this.totalPrice = totalPrice != null ? totalPrice : 0;
        this.totalPriceUSD = totalPriceUSD != null ? totalPriceUSD : BigDecimal.valueOf(0);
        this.salesCnt = salesCnt;
    }
}