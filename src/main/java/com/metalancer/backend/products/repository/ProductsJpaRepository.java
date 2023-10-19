package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsJpaRepository extends JpaRepository<ProductsEntity, Long> {

    Optional<ProductsEntity> findBySharedLinkContains(String sharedLink);

    int countAllByCreatorEntity(CreatorEntity creatorEntity);

    Page<ProductsEntity> findAllByCreatorEntity(CreatorEntity creatorEntity, Pageable pageable);

    Page<ProductsEntity> findAllByCreatorEntityAndStatus(CreatorEntity creatorEntity,
        DataStatus status,
        Pageable pageable);

    Page<ProductsEntity> findAllByStatusOrderByCreatedAtDesc(DataStatus status, Pageable pageable);

    Page<ProductsEntity> findAllByPriceAndStatusOrderByViewCntDesc(int price, DataStatus status,
        Pageable pageable);

    Page<ProductsEntity> findAllByPriceIsGreaterThanAndStatusOrderByViewCntDesc(int price,
        DataStatus status,
        Pageable pageable);
}
