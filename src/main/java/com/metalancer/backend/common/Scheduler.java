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

    @Scheduled(cron = "0 0 0 * * *") //정오
    public void updateExchangeRate() {
        BigDecimal exchangeRate = exchangeRateUtils.getExchangeRate();
        log.info(exchangeRate.toString());
        ExchangeRatesEntity exchangeRatesEntity = ExchangeRatesEntity.builder().amount(exchangeRate)
            .build();
        exchangeRatesJpaRepository.save(exchangeRatesEntity);
    }

}
