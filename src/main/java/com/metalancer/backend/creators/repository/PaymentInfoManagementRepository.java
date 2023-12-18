package com.metalancer.backend.creators.repository;

import com.metalancer.backend.creators.entity.PaymentInfoManagementEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.Optional;

public interface PaymentInfoManagementRepository {

    Optional<PaymentInfoManagementEntity> findByCreatorEntity(CreatorEntity creatorEntity);

    void delete(PaymentInfoManagementEntity paymentInfoManagementEntity);

    void save(PaymentInfoManagementEntity createdPaymentInfoManagementEntity);
}
