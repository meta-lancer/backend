package com.metalancer.backend.orders.domain;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EachSalesReport {

    private Long productsId;
    private List<DaySalesReport> daySalesReports;
    private int viewCnt;
    private int cartCnt;
    private int totalSalesCnt;
    private Integer totalPriceKRW;
    private BigDecimal totalPriceUSD;

    @Builder
    public EachSalesReport(Long productsId, List<DaySalesReport> daySalesReports, int viewCnt,
        int cartCnt, int totalSalesCnt, Integer totalPriceKRW, BigDecimal totalPriceUSD) {
        this.productsId = productsId;
        this.daySalesReports = daySalesReports;
        this.viewCnt = viewCnt;
        this.cartCnt = cartCnt;
        this.totalSalesCnt = totalSalesCnt;
        this.totalPriceKRW = totalPriceKRW;
        this.totalPriceUSD = totalPriceUSD;
    }

}