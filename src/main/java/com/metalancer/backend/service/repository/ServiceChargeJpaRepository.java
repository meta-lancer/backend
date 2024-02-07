package com.metalancer.backend.service.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ServiceChargesType;
import com.metalancer.backend.service.entity.ServiceChargeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceChargeJpaRepository extends
    JpaRepository<ServiceChargeEntity, Long> {

    Optional<ServiceChargeEntity> findByName(ServiceChargesType name);

    List<ServiceChargeEntity> findAllByStatus(DataStatus status);
}
