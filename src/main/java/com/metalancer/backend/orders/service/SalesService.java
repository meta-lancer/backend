package com.metalancer.backend.orders.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.orders.domain.DaySalesReport;
import java.util.List;

public interface SalesService {

    List<DaySalesReport> getDaySalesReport(PrincipalDetails user, PeriodType periodType);

    List<DaySalesReport> getDaySalesReportByExcel(PrincipalDetails user, String beginDate,
        String endDate);
}