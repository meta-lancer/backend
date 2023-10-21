package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductsJpaRepository extends JpaRepository<ProductsEntity, Long> {

    Optional<ProductsEntity> findBySharedLinkContains(String sharedLink);

    int countAllByCreatorEntity(CreatorEntity creatorEntity);

    Page<ProductsEntity> findAllByCreatorEntityAndStatus(CreatorEntity creatorEntity,
                                                         Pageable pageable, DataStatus status);

    Page<ProductsEntity> findAllByCreatorEntityAndStatus(CreatorEntity creatorEntity,
                                                         DataStatus status,
                                                         Pageable pageable);

    Page<ProductsEntity> findAllByStatusOrderByCreatedAtDesc(DataStatus status, Pageable pageable);

    Page<ProductsEntity> findAllByPriceAndStatusAndCreatedAtBetweenOrderByViewCntDesc(
            int price,
            DataStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    Page<ProductsEntity> findAllByPriceIsGreaterThanAndStatusAndCreatedAtBetweenOrderByViewCntDesc(int price,
                                                                                                   DataStatus status,
                                                                                                   LocalDateTime startDate,
                                                                                                   LocalDateTime endDate,
                                                                                                   Pageable pageable);
}
