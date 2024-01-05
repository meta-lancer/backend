package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.SettlementEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SettlementRepository {

    Page<SettlementEntity> findAllByCreator(CreatorEntity creatorEntity, Pageable pageable);

    LocalDateTime getRecentSettlementRequestDate(CreatorEntity creatorEntity);
}
