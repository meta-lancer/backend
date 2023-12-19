package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementJpaRepository extends JpaRepository<SettlementEntity, Long> {

}
