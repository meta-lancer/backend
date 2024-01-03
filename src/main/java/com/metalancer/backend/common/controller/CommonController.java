package com.metalancer.backend.common.controller;

import com.metalancer.backend.external.exchange.ExchangeRatesEntity;
import com.metalancer.backend.external.exchange.ExchangeRatesJpaRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@Tag(name = "Common", description = "")
@RequestMapping
@RequiredArgsConstructor
public class CommonController {

    private final ExchangeRatesJpaRepository exchangeRatesJpaRepository;

    @GetMapping("/")
    public String get() {
        return "get";
    }

    @GetMapping("/api/exchange")
    public BigDecimal getExchangeRate() {
        Optional<ExchangeRatesEntity> optionalExchangeRatesEntity = exchangeRatesJpaRepository.getFirstByOrderByCreatedAtDesc();
        if (optionalExchangeRatesEntity.isPresent()) {
            return optionalExchangeRatesEntity.get().getAmount();
        } else {
            return BigDecimal.valueOf(1300);
        }
    }
}
