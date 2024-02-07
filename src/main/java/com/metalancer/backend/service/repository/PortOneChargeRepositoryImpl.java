package com.metalancer.backend.service.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.PaymentType;
import com.metalancer.backend.common.exception.InvalidParamException;
import com.metalancer.backend.service.entity.PortOneChargeEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PortOneChargeRepositoryImpl implements PortOneChargeRepository {

    private final PortOneChargeJpaRepository portOneChargeJpaRepository;

    @Override
    public BigDecimal getChargeRate(PaymentType paymentType) {
        Optional<PortOneChargeEntity> portOneChargeEntityOptional = portOneChargeJpaRepository.findByPgName(
            paymentType);
        if (portOneChargeEntityOptional.isEmpty()) {
            throw new InvalidParamException("paymentType: ", ErrorCode.INVALID_PARAMETER);
        }
        return portOneChargeEntityOptional.get().getChargeRate();
    }

    @Override
    public List<PortOneChargeEntity> findAllByStatus(DataStatus dataStatus) {
        return portOneChargeJpaRepository.findAllByStatus(dataStatus);
    }

    @Override
    public Optional<PortOneChargeEntity> findById(Long chargeId) {
        return portOneChargeJpaRepository.findById(chargeId);
    }
}
