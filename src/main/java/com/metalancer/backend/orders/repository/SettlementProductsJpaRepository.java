package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.SettlementProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementProductsJpaRepository extends
    JpaRepository<SettlementProductsEntity, Long> {

}
