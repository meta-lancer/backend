package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.creators.entity.SettlementEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementJpaRepository extends JpaRepository<SettlementEntity, Long> {

    Page<SettlementEntity> findAllByCreatorEntityAndStatus(CreatorEntity creatorEntity,
        DataStatus status, Pageable pageable);

    Optional<SettlementEntity> findFirstByCreatorEntityOrderByCreatedAtDesc(
        CreatorEntity creatorEntity);
}
