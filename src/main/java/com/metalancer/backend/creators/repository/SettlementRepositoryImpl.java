package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.creators.entity.SettlementEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SettlementRepositoryImpl implements SettlementRepository {

    private final SettlementJpaRepository settlementJpaRepository;

    @Override
    public Page<SettlementEntity> findAllByCreator(CreatorEntity creatorEntity, Pageable pageable) {
        return settlementJpaRepository.findAllByCreatorEntityAndStatus(creatorEntity,
            DataStatus.ACTIVE, pageable);
    }

    @Override
    public LocalDateTime getRecentSettlementRequestDate(CreatorEntity creatorEntity) {
        Optional<SettlementEntity> recentSettlementEntity = settlementJpaRepository.findFirstByCreatorEntityOrderByCreatedAtDesc(
            creatorEntity);
        return recentSettlementEntity.map(SettlementEntity::getCreatedAt).orElse(null);
    }

    @Override
    public void save(SettlementEntity settlementEntity) {
        settlementJpaRepository.save(settlementEntity);
    }

    @Override
    public Optional<SettlementEntity> findById(Long id) {
        return settlementJpaRepository.findById(id);
    }

    @Override
    public Page<SettlementEntity> findAllBySettlementStatus(SettlementStatus settlementStatus,
        Pageable pageable) {
        return settlementJpaRepository.findAllBySettlementStatus(settlementStatus, pageable);
    }
}
