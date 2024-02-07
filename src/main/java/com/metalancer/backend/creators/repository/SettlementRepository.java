package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.creators.entity.SettlementEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SettlementRepository {

    Page<SettlementEntity> findAllByCreator(CreatorEntity creatorEntity, Pageable pageable);

    LocalDateTime getRecentSettlementRequestDate(CreatorEntity creatorEntity);

    void save(SettlementEntity settlementEntity);

    Optional<SettlementEntity> findById(Long id);

    Page<SettlementEntity> findAllBySettlementStatus(SettlementStatus settlementStatus,
        Pageable pageable);
}
