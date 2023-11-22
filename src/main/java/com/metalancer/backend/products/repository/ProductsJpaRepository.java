package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductsJpaRepository extends JpaRepository<ProductsEntity, Long> {

    Optional<ProductsEntity> findBySharedLinkContains(String sharedLink);

    int countAllByCreatorEntityAndStatus(CreatorEntity creatorEntity, DataStatus status);

    Page<ProductsEntity> findAllByCreatorEntityAndStatus(CreatorEntity creatorEntity,
        Pageable pageable, DataStatus status);

    Page<ProductsEntity> findAllByCreatorEntityAndStatus(CreatorEntity creatorEntity,
        DataStatus status,
        Pageable pageable);

    Page<ProductsEntity> findAllByStatusAndProductsAssetFileEntitySuccessAndCreatedAtBetweenOrderByCreatedAtDesc(
        DataStatus status, Boolean success,
        LocalDateTime startDate,
        LocalDateTime endDate, Pageable pageable);

    Page<ProductsEntity> findAllBySalePriceNotNullAndStatusAndProductsAssetFileEntitySuccessAndCreatedAtBetweenOrderByCreatedAtDesc(
        DataStatus status, Boolean success,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable);

    Page<ProductsEntity> findAllByPriceAndStatusAndProductsAssetFileEntitySuccessAndCreatedAtBetweenOrderByViewCntDesc(
        int price,
        DataStatus status,
        Boolean success,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable
    );

    Page<ProductsEntity> findAllByPriceIsGreaterThanAndStatusAndProductsAssetFileEntitySuccessAndCreatedAtBetweenOrderByViewCntDesc(
        int price,
        DataStatus status,
        Boolean success,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable);


    @Query("SELECT DISTINCT pt.productsEntity FROM products_tag pt WHERE pt.name IN :tagList and pt.productsEntity.status = :status")
    Page<ProductsEntity> findDistinctProductsByTagNamesAndStatus(
        @Param("tagList") List<String> tagList, @Param("status") DataStatus status,
        Pageable pageable);

    long countAllBy();

    Page<ProductsEntity> findAllByStatusOrderByCreatedAtDesc(DataStatus status, Pageable pageable);
}
