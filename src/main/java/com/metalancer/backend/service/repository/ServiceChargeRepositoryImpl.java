package com.metalancer.backend.service.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.ServiceChargesType;
import com.metalancer.backend.common.exception.InvalidParamException;
import com.metalancer.backend.service.entity.ServiceChargeEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ServiceChargeRepositoryImpl implements ServiceChargeRepository {

    private final ServiceChargeJpaRepository serviceChargeJpaRepository;

    @Override
    public BigDecimal getChargeRate(ServiceChargesType serviceChargesType) {
        Optional<ServiceChargeEntity> serviceChargeEntityOptional = serviceChargeJpaRepository.findByName(
            serviceChargesType);
        if (serviceChargeEntityOptional.isEmpty()) {
            throw new InvalidParamException("serviceChargesType: ", ErrorCode.INVALID_PARAMETER);
        }
        return serviceChargeEntityOptional.get().getChargeRate();
    }

    @Override
    public List<ServiceChargeEntity> findAllByStatus(DataStatus dataStatus) {
        return serviceChargeJpaRepository.findAllByStatus(dataStatus);
    }

    @Override
    public Optional<ServiceChargeEntity> findById(Long chargeId) {
        return serviceChargeJpaRepository.findById(chargeId);
    }
}
