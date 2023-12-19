package com.metalancer.backend.orders.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DaySalesReport {

    private final String day;
    private final int totalPrice;
    private final int salesCnt;

    @Builder
    public DaySalesReport(String day, Integer totalPrice, int salesCnt) {
        this.day = day;
        this.totalPrice = totalPrice != null ? totalPrice : 0;
        this.salesCnt = salesCnt;
    }
}