package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.creators.domain.DaySalesReport;
import com.metalancer.backend.creators.domain.EachSalesReport;
import com.metalancer.backend.creators.domain.SettlementRecordList;
import com.metalancer.backend.creators.domain.SettlementReportList;
import com.metalancer.backend.creators.domain.SettlementRequestInfo;
import com.metalancer.backend.creators.domain.SettlementRequestList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalesService {

    List<DaySalesReport> getDaySalesReport(PrincipalDetails user, PeriodType periodType);

    List<DaySalesReport> getDaySalesReportByExcel(PrincipalDetails user, String beginDate,
        String endDate);

    Page<SettlementReportList> getSettlementReportList(PrincipalDetails user, Pageable pageable);

    Page<SettlementRecordList> getSettlementRecordList(PrincipalDetails user, Pageable pageable);

    EachSalesReport getSettlementProductsReport(Long productsId, PrincipalDetails user,
        PeriodType periodType);

    List<DaySalesReport> getProductsDaySalesReportByExcel(Long productsId,
        PrincipalDetails user, String beginDate,
        String endDate);

    Boolean checkSettlementRequestAvailable(PrincipalDetails user);

    Page<SettlementRequestList> getSettlementRequestList(PrincipalDetails user, Pageable pageable);

    SettlementRequestInfo getSettlementRequestInfo(PrincipalDetails user);

    Boolean createSettlementRequest(PrincipalDetails user);
}