package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.orders.entity.SettlementEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementJpaRepository extends JpaRepository<SettlementEntity, Long> {

    Page<SettlementEntity> findAllByCreatorEntityAndStatus(CreatorEntity creatorEntity,
        DataStatus status, Pageable pageable);
}
