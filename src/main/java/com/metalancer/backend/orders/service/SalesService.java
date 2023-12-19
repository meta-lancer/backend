package com.metalancer.backend.orders.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.orders.domain.DaySalesReport;
import com.metalancer.backend.orders.domain.SettlementReportList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalesService {

    List<DaySalesReport> getDaySalesReport(PrincipalDetails user, PeriodType periodType);

    List<DaySalesReport> getDaySalesReportByExcel(PrincipalDetails user, String beginDate,
        String endDate);

    Page<SettlementReportList> getSettlementReportList(PrincipalDetails user, Pageable pageable);
}