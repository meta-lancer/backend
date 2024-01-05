package com.metalancer.backend.orders.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EachSalesReport {

    private Long productsId;
    private List<DaySalesReport> daySalesReports;
    private int viewCnt;
    private int cartCnt;


    @Builder
    public EachSalesReport(Long productsId, List<DaySalesReport> daySalesReports, int viewCnt,
        int cartCnt) {
        this.productsId = productsId;
        this.daySalesReports = daySalesReports;
        this.viewCnt = viewCnt;
        this.cartCnt = cartCnt;
    }

}