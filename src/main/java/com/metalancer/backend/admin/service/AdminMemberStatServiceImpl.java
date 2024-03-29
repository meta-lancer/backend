package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.MemberRegisterDailyStat;
import com.metalancer.backend.admin.domain.MemberRegisterStat;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.users.repository.LoginLogsRepository;
import com.metalancer.backend.users.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
public class AdminMemberStatServiceImpl implements AdminMemberStatService {

    private final UserRepository userRepository;
    private final CreatorRepository creatorRepository;
    private final LoginLogsRepository loginLogsRepository;

    @Override
    public MemberRegisterStat getAdminMemberStat() {
        Integer totalUserCnt = userRepository.countUserBy();
        Integer normalUserCnt = userRepository.getNormalUserCnt();
        Integer creatorUserCnt = userRepository.getCreatorUserCnt();
        Integer withdrewCnt = userRepository.countAllByStatus(DataStatus.DELETED);
        Integer emailRegisterCnt = userRepository.countAllByLoginType(LoginType.NORMAL);
        Integer googleRegisterCnt = userRepository.countAllByLoginType(LoginType.GOOGLE);
        Integer naverRegisterCnt = userRepository.countAllByLoginType(LoginType.NAVER);
        Integer kakaoRegisterCnt = userRepository.countAllByLoginType(LoginType.KAKAO);
        return MemberRegisterStat.builder().totalUserCnt(totalUserCnt).normalUserCnt(normalUserCnt)
            .creatorUserCnt(creatorUserCnt)
            .withdrewCnt(withdrewCnt).emailRegisterCnt(emailRegisterCnt)
            .googleRegisterCnt(googleRegisterCnt).naverRegisterCnt(naverRegisterCnt)
            .kakaoRegisterCnt(kakaoRegisterCnt).build();
    }

    @Override
    public List<MemberRegisterDailyStat> getAdminMemberRegisterDailyStat(String beginDate,
        String endDate, PeriodType periodType) {
        // beginDate, endDate는 아직 굳이...
        LocalDateTime endAt = LocalDateTime.now();
        LocalDateTime beginAt = getBeginAtBasedOnPeriodType(periodType, endAt);
        List<MemberRegisterDailyStat> response = new ArrayList<>();
        if (!periodType.equals(PeriodType.YEARLY)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (LocalDateTime date = beginAt; date.isBefore(endAt); date = date.plusDays(1)) {
                setMemberRegisterDailyStatBasedOnDate(dateFormatter, response, date);
            }
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM월");
            for (LocalDateTime date = beginAt; date.isBefore(endAt); date = date.plusMonths(1)) {
                String formattedDate = date.format(dateFormatter);
                LocalDateTime startOfNextMonth = date.plusMonths(1);
                Integer registerCnt = userRepository.getRegisterCntByDate(date, startOfNextMonth);
                Integer creatorRegisterCnt = creatorRepository.getRegisterCntByDate(date,
                    startOfNextMonth);
                Integer loginCnt = loginLogsRepository.getLoginCntYearly(date,
                    startOfNextMonth);
                MemberRegisterDailyStat dailyStat = MemberRegisterDailyStat.builder()
                    .date(formattedDate)
                    .registerCnt(registerCnt)
                    .creatorRegisterCnt(creatorRegisterCnt).loginCnt(loginCnt).build();
                response.add(dailyStat);
            }
        }
        return response;
    }

    private void setMemberRegisterDailyStatBasedOnDate(DateTimeFormatter dateFormatter,
        List<MemberRegisterDailyStat> response, LocalDateTime date) {
        String formattedDate = date.format(dateFormatter);
        LocalDateTime startOfNextDay = date.plusDays(1);
        Integer registerCnt = userRepository.getRegisterCntByDate(date, startOfNextDay);
        Integer creatorRegisterCnt = creatorRepository.getRegisterCntByDate(date, startOfNextDay);
        Integer loginCnt = loginLogsRepository.getLoginCntDaily(date,
            startOfNextDay);
        MemberRegisterDailyStat dailyStat = MemberRegisterDailyStat.builder()
            .date(formattedDate)
            .registerCnt(registerCnt)
            .creatorRegisterCnt(creatorRegisterCnt).loginCnt(loginCnt).build();
        response.add(dailyStat);
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
}