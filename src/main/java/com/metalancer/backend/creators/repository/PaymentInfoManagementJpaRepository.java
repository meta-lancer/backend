package com.metalancer.backend.creators.repository;

import com.metalancer.backend.creators.entity.PaymentInfoManagementEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentInfoManagementJpaRepository extends
    JpaRepository<PaymentInfoManagementEntity, Long> {

    Optional<PaymentInfoManagementEntity> findByCreatorEntity(CreatorEntity creatorEntity);
}
