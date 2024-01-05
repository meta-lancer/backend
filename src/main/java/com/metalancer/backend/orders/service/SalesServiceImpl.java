package com.metalancer.backend.orders.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.orders.domain.DaySalesReport;
import com.metalancer.backend.orders.domain.EachSalesReport;
import com.metalancer.backend.orders.domain.SettlementRecordList;
import com.metalancer.backend.orders.domain.SettlementReportList;
import com.metalancer.backend.orders.entity.SettlementEntity;
import com.metalancer.backend.orders.repository.ProductsSalesRepository;
import com.metalancer.backend.orders.repository.SettlementProductsRepository;
import com.metalancer.backend.orders.repository.SettlementRepository;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.CartRepository;
import com.metalancer.backend.users.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class SalesServiceImpl implements SalesService {

    private final ProductsSalesRepository productsSalesRepository;
    private final UserRepository userRepository;
    private final CreatorRepository creatorRepository;
    private final ProductsRepository productsRepository;
    private final SettlementRepository settlementRepository;
    private final SettlementProductsRepository settlementProductsRepository;
    private final CartRepository cartRepository;

    @Override
    public List<DaySalesReport> getDaySalesReport(PrincipalDetails user, PeriodType periodType) {
        CreatorEntity creatorEntity = getCreatorEntity(user);
        LocalDateTime endAt = LocalDateTime.now();
        LocalDateTime beginAt = getBeginAtBasedOnPeriodType(periodType, endAt);

        List<DaySalesReport> response = new ArrayList<>();
        if (!periodType.equals(PeriodType.YEARLY)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (LocalDateTime date = beginAt; date.isBefore(endAt); date = date.plusDays(1)) {
                setTotalPriceAndSalesCntBasedOnDate(creatorEntity, dateFormatter, response, date);
            }
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM월");
            for (LocalDateTime date = beginAt; date.isBefore(endAt); date = date.plusMonths(1)) {
                String formattedDate = date.format(dateFormatter);
                LocalDateTime startOfNextMonth = date.plusMonths(1);
                BigDecimal totalPriceKRW = (productsSalesRepository.getTotalPriceByCreatorAndDate(
                    creatorEntity, date, startOfNextMonth, CurrencyType.KRW));
                BigDecimal totalPriceUSD = productsSalesRepository.getTotalPriceByCreatorAndDate(
                    creatorEntity, date, startOfNextMonth, CurrencyType.USD);
                int salesCnt = productsSalesRepository.getSalesCntByCreatorAndDate(
                    creatorEntity, date, startOfNextMonth);
                DaySalesReport daySalesReport = DaySalesReport.builder().day(formattedDate)
                    .totalPrice(convertBigDecimalToInteger(totalPriceKRW))
                    .totalPriceUSD(totalPriceUSD)
                    .salesCnt(salesCnt)
                    .build();
                response.add(daySalesReport);
            }
        }
        return response;
    }

    public Integer convertBigDecimalToInteger(BigDecimal doubleValue) {
        if (doubleValue == null) {
            return null;
        }
        return doubleValue.intValue();
    }

    private void setTotalPriceAndSalesCntBasedOnDate(CreatorEntity creatorEntity,
        DateTimeFormatter dateFormatter,
        List<DaySalesReport> response, LocalDateTime date) {
        String formattedDate = date.format(dateFormatter);
        LocalDateTime startOfNextDay = date.plusDays(1);
        BigDecimal totalPriceKRW = productsSalesRepository.getTotalPriceByCreatorAndDate(
            creatorEntity, date, startOfNextDay, CurrencyType.KRW);
        BigDecimal totalPriceUSD = productsSalesRepository.getTotalPriceByCreatorAndDate(
            creatorEntity, date, startOfNextDay, CurrencyType.USD);
        int salesCnt = productsSalesRepository.getSalesCntByCreatorAndDate(
            creatorEntity, date, startOfNextDay);
        DaySalesReport daySalesReport = DaySalesReport.builder().day(formattedDate)
            .totalPrice(convertBigDecimalToInteger(totalPriceKRW)).totalPriceUSD(totalPriceUSD)
            .salesCnt(salesCnt).build();
        response.add(daySalesReport);
    }

    private void setProductsTotalPriceAndSalesCntBasedOnDate(CreatorEntity creatorEntity,
        ProductsEntity productsEntity,
        DateTimeFormatter dateFormatter,
        List<DaySalesReport> response, LocalDateTime date) {
        String formattedDate = date.format(dateFormatter);
        LocalDateTime startOfNextDay = date.plusDays(1);
        BigDecimal totalPriceKRW = (productsSalesRepository.getProductsTotalPriceByCreatorAndDate(
            creatorEntity, productsEntity, date, startOfNextDay, CurrencyType.KRW));
        BigDecimal totalPriceUSD = productsSalesRepository.getProductsTotalPriceByCreatorAndDate(
            creatorEntity, productsEntity, date, startOfNextDay, CurrencyType.USD);
        int salesCnt = productsSalesRepository.getProductsSalesCntByCreatorAndDate(
            creatorEntity, productsEntity, date, startOfNextDay);
        DaySalesReport daySalesReport = DaySalesReport.builder().day(formattedDate)
            .totalPrice(convertBigDecimalToInteger(totalPriceKRW)).totalPriceUSD(totalPriceUSD)
            .salesCnt(salesCnt).build();
        response.add(daySalesReport);
    }

    @NotNull
    private static LocalDateTime getBeginAtBasedOnPeriodType(PeriodType periodType,
        LocalDateTime endAt) {
        return periodType.equals(PeriodType.WEEKLY) ? endAt.minusWeeks(1)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .plusDays(1) :
            periodType.equals(PeriodType.MONTHLY) ?
                endAt.minusMonths(1)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0)
                    .plusDays(1) :
                endAt.minusYears(1)
                    .withDayOfMonth(1)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0)
                    .plusMonths(1);
    }

    @Override
    public List<DaySalesReport> getDaySalesReportByExcel(PrincipalDetails user, String beginDate,
        String endDate) {
        CreatorEntity creatorEntity = getCreatorEntity(user);
        LocalDateTime beginAt = Time.convertDateToLocalDateTime(beginDate);
        LocalDateTime endAt = Time.convertDateToLocalDateTime(endDate);
        endAt = endAt.plusDays(1);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<DaySalesReport> response = new ArrayList<>();
        for (LocalDateTime date = beginAt; date.isBefore(endAt); date = date.plusDays(1)) {
            setTotalPriceAndSalesCntBasedOnDate(creatorEntity, dateFormatter, response, date);
        }
        Collections.reverse(response);
        return response;
    }

    @Override
    public Page<SettlementReportList> getSettlementReportList(PrincipalDetails user,
        Pageable pageable) {
        CreatorEntity creatorEntity = getCreatorEntity(user);
        Page<ProductsEntity> productsEntityList = productsRepository.findAllByCreator(creatorEntity,
            pageable);
        long totalCnt = productsEntityList.getTotalElements();
        List<SettlementReportList> reportLists = new ArrayList<>();

        for (ProductsEntity productsEntity : productsEntityList.getContent()) {
            // 정산 요청 - 정산 요청된 상품 목록 이런식으로...!
            // 판매한 갯수와 정산 요청된 갯수가 같다면 true, 아니면 false(판매갯수 0이라면도 추가!)
            boolean hasSettled = false;
            int totalSalesCnt = productsSalesRepository.countAllByProducts(productsEntity);
            int totalSettlementCnt = settlementProductsRepository.countAllByProducts(
                productsEntity);
            boolean noMoreSettlement = totalSalesCnt == totalSettlementCnt;
            if (totalSalesCnt > 0 && noMoreSettlement) {
                hasSettled = true;
            }

            SettlementReportList settlementReport = productsEntity.toSettlementReportList(
                hasSettled);
            reportLists.add(settlementReport);
        }
        return new PageImpl<>(reportLists, pageable, totalCnt);
    }

    @Override
    public Page<SettlementRecordList> getSettlementRecordList(PrincipalDetails user,
        Pageable pageable) {
        CreatorEntity creatorEntity = getCreatorEntity(user);
        Page<SettlementEntity> settlementEntityList = settlementRepository.findAllByCreator(
            creatorEntity, pageable);
        return settlementEntityList.map(SettlementEntity::toSettlementRecordList);
    }

    @Override
    public EachSalesReport getSettlementProductsReport(Long productsId, PrincipalDetails user,
        PeriodType periodType) {
        List<DaySalesReport> daySalesReports = new ArrayList<>();
        CreatorEntity creatorEntity = getCreatorEntity(user);

        LocalDateTime endAt = LocalDateTime.now();
        LocalDateTime beginAt = getBeginAtBasedOnPeriodType(periodType, endAt);

        ProductsEntity productsEntity = productsRepository.findProductByIdWithoutStatus(productsId);

        if (!productsEntity.getCreatorEntity().equals(creatorEntity)) {
            throw new BaseException(ErrorCode.IS_NOT_WRITER);
        }

        if (!periodType.equals(PeriodType.YEARLY)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (LocalDateTime date = beginAt; date.isBefore(endAt); date = date.plusDays(1)) {
                setProductsTotalPriceAndSalesCntBasedOnDate(creatorEntity, productsEntity,
                    dateFormatter, daySalesReports, date);
            }
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM월");
            for (LocalDateTime date = beginAt; date.isBefore(endAt); date = date.plusMonths(1)) {
                String formattedDate = date.format(dateFormatter);
                LocalDateTime startOfNextMonth = date.plusMonths(1);
                BigDecimal totalPriceKRW = (productsSalesRepository.getProductsTotalPriceByCreatorAndDate(
                    creatorEntity, productsEntity, date, startOfNextMonth, CurrencyType.KRW));
                BigDecimal totalPriceUSD = productsSalesRepository.getProductsTotalPriceByCreatorAndDate(
                    creatorEntity, productsEntity, date, startOfNextMonth, CurrencyType.USD);
                int salesCnt = productsSalesRepository.getProductsSalesCntByCreatorAndDate(
                    creatorEntity, productsEntity, date, startOfNextMonth);
                DaySalesReport daySalesReport = DaySalesReport.builder().day(formattedDate)
                    .totalPrice(convertBigDecimalToInteger(totalPriceKRW))
                    .totalPriceUSD(totalPriceUSD)
                    .salesCnt(salesCnt)
                    .build();
                daySalesReports.add(daySalesReport);
            }
        }

        int viewCnt = productsEntity.getViewCnt();
        int cartCnt = cartRepository.countAllByProducts(productsEntity);
        int totalSalesCnt = productsSalesRepository.countAllByProducts(productsEntity);
        BigDecimal totalPriceKRW = productsSalesRepository.getProductsTotalPriceByCreator(
            creatorEntity, productsEntity, CurrencyType.KRW);
        BigDecimal totalPriceUSD = productsSalesRepository.getProductsTotalPriceByCreator(
            creatorEntity, productsEntity, CurrencyType.USD);
        return EachSalesReport.builder().productsId(productsEntity.getId())
            .daySalesReports(daySalesReports).viewCnt(viewCnt)
            .cartCnt(cartCnt).totalSalesCnt(totalSalesCnt).totalPriceKRW(totalPriceKRW.intValue())
            .totalPriceUSD(totalPriceUSD).build();
    }

    private CreatorEntity getCreatorEntity(PrincipalDetails user) {
        User foundUser = user.getUser();
        return creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
    }

    @Override
    public List<DaySalesReport> getProductsDaySalesReportByExcel(Long productsId,
        PrincipalDetails user, String beginDate,
        String endDate) {
        CreatorEntity creatorEntity = getCreatorEntity(user);
        ProductsEntity productsEntity = productsRepository.findProductByIdWithoutStatus(productsId);

        LocalDateTime beginAt = Time.convertDateToLocalDateTime(beginDate);
        LocalDateTime endAt = Time.convertDateToLocalDateTime(endDate);
        endAt = endAt.plusDays(1);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<DaySalesReport> response = new ArrayList<>();
        for (LocalDateTime date = beginAt; date.isBefore(endAt); date = date.plusDays(1)) {
            setProductsTotalPriceAndSalesCntBasedOnDate(creatorEntity, productsEntity,
                dateFormatter, response, date);
        }
        Collections.reverse(response);
        return response;
    }
}