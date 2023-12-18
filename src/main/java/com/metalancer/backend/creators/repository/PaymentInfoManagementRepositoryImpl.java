package com.metalancer.backend.creators.repository;

import com.metalancer.backend.creators.entity.PaymentInfoManagementEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PaymentInfoManagementRepositoryImpl implements PaymentInfoManagementRepository {

    private final PaymentInfoManagementJpaRepository paymentInfoManagementJpaRepository;

    @Override
    public Optional<PaymentInfoManagementEntity> findByCreatorEntity(
        CreatorEntity creatorEntity) {
        return paymentInfoManagementJpaRepository.findByCreatorEntity(creatorEntity);
    }

    @Override
    public void delete(PaymentInfoManagementEntity paymentInfoManagementEntity) {
        paymentInfoManagementJpaRepository.delete(paymentInfoManagementEntity);
    }
}
