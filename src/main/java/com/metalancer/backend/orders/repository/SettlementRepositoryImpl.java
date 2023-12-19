package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.orders.entity.SettlementEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
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
}
