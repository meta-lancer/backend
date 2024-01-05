package com.metalancer.backend.external.exchange;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRatesJpaRepository extends JpaRepository<ExchangeRatesEntity, Long> {

    Optional<ExchangeRatesEntity> getFirstByOrderByCreatedAtDesc();
}
