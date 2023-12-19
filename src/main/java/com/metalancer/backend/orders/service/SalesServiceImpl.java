package com.metalancer.backend.orders.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.orders.domain.DaySalesReport;
import com.metalancer.backend.orders.repository.ProductsSalesRepository;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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

    @Override
    public List<DaySalesReport> getDaySalesReport(PrincipalDetails user, PeriodType periodType) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        LocalDateTime endAt = LocalDateTime.now();
        LocalDateTime beginAt = getBeginAtBasedOnPeriodType(periodType, endAt);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<DaySalesReport> response = new ArrayList<>();

        for (LocalDateTime date = beginAt; date.isBefore(endAt); date = date.plusDays(1)) {
            String formattedDate = date.format(dateFormatter);
            LocalDateTime startOfNextDay = date.plusDays(1);
            Integer totalPrice = productsSalesRepository.getTotalPriceByCreatorAndDate(
                creatorEntity, date, startOfNextDay);
            int salesCnt = productsSalesRepository.getSalesCntByCreatorAndDate(
                creatorEntity, date, startOfNextDay);
            DaySalesReport daySalesReport = DaySalesReport.builder().day(formattedDate)
                .totalPrice(totalPrice).salesCnt(salesCnt).build();
            response.add(daySalesReport);
        }
        Collections.reverse(response);
        return response;
    }

    @NotNull
    private static LocalDateTime getBeginAtBasedOnPeriodType(PeriodType periodType,
        LocalDateTime endAt) {
        LocalDateTime beginAt = periodType.equals(PeriodType.WEEKLY) ? endAt.minusWeeks(1)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0) :
            endAt.minusMonths(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        return beginAt;
    }

    @Override
    public List<DaySalesReport> getDaySalesReportByExcel(PrincipalDetails user, String beginDate,
        String endDate) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        LocalDateTime beginAt = Time.convertDateToLocalDateTime(beginDate);
        LocalDateTime endAt = Time.convertDateToLocalDateTime(endDate);
        return null;
    }
}