package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ProductsType;
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

    Page<ProductsEntity> findAllByCreatorEntityAndProductsAssetFileEntitySuccessAndStatus(
        CreatorEntity creatorEntity,
        Boolean success,
        Pageable pageable, DataStatus status);

    Page<ProductsEntity> findAllByCreatorEntityAndProductsTypeAndStatus(
        CreatorEntity creatorEntity,
        ProductsType productsType,
        DataStatus status, Pageable pageable);

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


    @Query("SELECT DISTINCT pt.productsEntity FROM products_tag pt WHERE pt.name IN :tagList and pt.productsEntity.status = :status and pt.productsEntity.productsAssetFileEntity.success = true")
    Page<ProductsEntity> findDistinctProductsByTagNamesAndStatus(
        @Param("tagList") List<String> tagList, @Param("status") DataStatus status,
        Pageable pageable);

    @Query("SELECT DISTINCT pt.productsEntity FROM products_tag pt WHERE pt.name IN :tagList and pt.productsEntity.productsType = :productsType and pt.productsEntity.status = :status")
    Page<ProductsEntity> findDistinctProductsByTagNamesAndProductsTypeAndStatus(
        @Param("tagList") List<String> tagList, @Param("productsType") ProductsType productsType,
        @Param("status") DataStatus status,
        Pageable pageable);

    @Query("SELECT DISTINCT pt.productsEntity FROM products_tag pt WHERE pt.name IN :tagList and pt.productsEntity.status = :status and pt.productsEntity.productsAssetFileEntity.success = true and pt.productsEntity.title like concat('%', :keyword,'%') ")
    Page<ProductsEntity> findDistinctProductsByTagNamesAndStatusAndKeyword(
        @Param("tagList") List<String> tagList, @Param("status") DataStatus status,
        @Param("keyword") String keyword,
        Pageable pageable);

    long countAllBy();

    Page<ProductsEntity> findAllByStatusAndProductsAssetFileEntitySuccessOrderByCreatedAtDesc(
        DataStatus status, Boolean success, Pageable pageable);

    Page<ProductsEntity> findAllByStatusAndProductsTypeOrderByCreatedAtDesc(
        DataStatus status, ProductsType productsType, Pageable pageable);

    Page<ProductsEntity> findAllByStatusAndProductsAssetFileEntitySuccessAndTitleContainsOrderByCreatedAtDesc(
        DataStatus status, Boolean success, String keyword, Pageable pageable);

    Page<ProductsEntity> findAllByCreatorEntity(CreatorEntity creatorEntity, Pageable pageable);

    @Query("select p from products p where p.creatorEntity = :creatorEntity and p.status = 'ACTIVE' and (p.productsAssetFileEntity.success = true or p.productsType = 'REQUEST')")
    Page<ProductsEntity> findAllValidProductsByCreator(
        @Param("creatorEntity") CreatorEntity creatorEntity,
        Pageable pageable);

    Optional<ProductsEntity> findByIdAndStatus(Long productsId, DataStatus status);
}
