package com.metalancer.backend.service.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ServiceChargesType;
import com.metalancer.backend.service.entity.ServiceChargeEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ServiceChargeRepository {

    BigDecimal getChargeRate(ServiceChargesType serviceChargesType);

    List<ServiceChargeEntity> findAllByStatus(DataStatus dataStatus);

    Optional<ServiceChargeEntity> findById(Long chargeId);
}
