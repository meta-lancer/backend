package com.metalancer.backend.common;


import com.metalancer.backend.common.utils.ExchangeRateUtils;
import com.metalancer.backend.external.exchange.ExchangeRatesEntity;
import com.metalancer.backend.external.exchange.ExchangeRatesJpaRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class Scheduler {

    private final ExchangeRateUtils exchangeRateUtils;

    private final ExchangeRatesJpaRepository exchangeRatesJpaRepository;

    // 이용시 유의사항
    // 자정 => 안 먹힌다.
    // 비영업일의 데이터, 혹은 영업당일 11시 이전에 해당일의 데이터를 요청할 경우 null 값이 반환
    @Scheduled(cron = "0 0 12 * * 1-5")
    public void updateExchangeRate() {
        BigDecimal exchangeRate = exchangeRateUtils.getExchangeRate();
        log.info(exchangeRate.toString());
        ExchangeRatesEntity exchangeRatesEntity = ExchangeRatesEntity.builder().amount(exchangeRate)
            .build();
        exchangeRatesJpaRepository.save(exchangeRatesEntity);
    }

}
