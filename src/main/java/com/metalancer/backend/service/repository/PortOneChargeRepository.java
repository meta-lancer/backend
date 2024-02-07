package com.metalancer.backend.service.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.PaymentType;
import com.metalancer.backend.service.entity.PortOneChargeEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PortOneChargeRepository {

    BigDecimal getChargeRate(PaymentType paymentType);

    List<PortOneChargeEntity> findAllByStatus(DataStatus dataStatus);

    Optional<PortOneChargeEntity> findById(Long chargeId);
}
