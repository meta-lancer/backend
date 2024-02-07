package com.metalancer.backend.service.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.PaymentType;
import com.metalancer.backend.service.entity.PortOneChargeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortOneChargeJpaRepository extends
    JpaRepository<PortOneChargeEntity, Long> {

    Optional<PortOneChargeEntity> findByPgName(PaymentType pgName);

    List<PortOneChargeEntity> findAllByStatus(DataStatus status);
}
