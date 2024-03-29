package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.creators.entity.ProductsSalesEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductsSalesJpaRepository extends JpaRepository<ProductsSalesEntity, Long> {

    @Query("select COALESCE(SUM(pse.price), 0) from product_sales pse where pse.creatorEntity = :creatorEntity and pse.createdAt between :startDate and :startOfNextDay and pse.currency = :currency")
    BigDecimal getTotalPriceByCreatorAndDate(@Param("creatorEntity") CreatorEntity creatorEntity,
        @Param("startDate") LocalDateTime startDate,
        @Param("startOfNextDay") LocalDateTime startOfNextDay,
        @Param("currency") CurrencyType currency
    );

    @Query("select count(pse) from product_sales pse where pse.creatorEntity = :creatorEntity and pse.createdAt between :startDate and :startOfNextDay")
    int getSalesCntByCreatorAndDate(@Param("creatorEntity") CreatorEntity creatorEntity,
        @Param("startDate") LocalDateTime startDate,
        @Param("startOfNextDay") LocalDateTime startOfNextDay);

    @Query("select COALESCE(SUM(pse.price), 0) from product_sales pse where pse.creatorEntity = :creatorEntity and pse.productsEntity = :products and pse.createdAt between :startDate and :startOfNextDay and pse.currency = :currency")
    BigDecimal getProductsTotalPriceByCreatorAndDate(
        @Param("creatorEntity") CreatorEntity creatorEntity,
        @Param("products") ProductsEntity products,
        @Param("startDate") LocalDateTime startDate,
        @Param("startOfNextDay") LocalDateTime startOfNextDay,
        @Param("currency") CurrencyType currency
    );

    @Query("select count(pse) from product_sales pse where pse.creatorEntity = :creatorEntity and pse.productsEntity = :products and pse.createdAt between :startDate and :startOfNextDay")
    int getProductsSalesCntByCreatorAndDate(@Param("creatorEntity") CreatorEntity creatorEntity,
        @Param("products") ProductsEntity products,
        @Param("startDate") LocalDateTime startDate,
        @Param("startOfNextDay") LocalDateTime startOfNextDay);

    int countAllByProductsEntity(ProductsEntity products);

    @Query("select COALESCE(SUM(pse.price), 0) from product_sales pse where pse.creatorEntity = :creatorEntity and pse.productsEntity = :products and pse.currency = :currency")
    BigDecimal getProductsTotalPriceByCreator(@Param("creatorEntity") CreatorEntity creatorEntity,
        @Param("products") ProductsEntity products,
        @Param("currency") CurrencyType currency);

    int countAllByCreatorEntityAndSettledIsFalse(CreatorEntity creatorEntity);

    Optional<ProductsSalesEntity> findFirstByCreatorEntityOrderByCreatedAtDesc(
        CreatorEntity creatorEntity);

    Page<ProductsSalesEntity> findAllByCreatorEntityAndSettledIsFalse(CreatorEntity creatorEntity,
        Pageable pageable);

    @Query("select DISTINCT pse.productsEntity from product_sales pse where pse.creatorEntity = :creatorEntity and pse.settled = false")
    List<ProductsEntity> findAllProductsDistinctByCreatorEntityAndSettledIsFalse(
        CreatorEntity creatorEntity);

    List<ProductsSalesEntity> findAllByCreatorEntityAndSettledIsFalse(
        CreatorEntity creatorEntity);

    @Query("select COALESCE(SUM(pse.price), 0) from product_sales pse where pse.creatorEntity = :creatorEntity and pse.currency = :currency")
    BigDecimal getTotalPriceByCreator(@Param("creatorEntity") CreatorEntity creatorEntity,
        @Param("currency") CurrencyType currency);

    @Query("select COALESCE(SUM(pse.price * pse.chargeRate), 0) from product_sales pse where pse.creatorEntity = :creatorEntity and pse.currency = :currency")
    BigDecimal getTotalPortoneChargesByCreator(@Param("creatorEntity") CreatorEntity creatorEntity,
        @Param("currency") CurrencyType currency);

    int countAllByProductsEntityAndSettledIsFalse(ProductsEntity productsEntity);

    @Query("select COALESCE(SUM(pse.price), 0) from product_sales pse where pse.creatorEntity = :creatorEntity and pse.productsEntity =:productsEntity and pse.currency = :currency and pse.settled = false")
    BigDecimal getTotalAmountByCreatorAndProductsAndSettledIsFalse(
        @Param("creatorEntity") CreatorEntity creatorEntity,
        @Param("productsEntity") ProductsEntity productsEntity,
        @Param("currency") CurrencyType currency);

    @Query("select COALESCE(SUM(pse.price * pse.chargeRate), 0) from product_sales pse where pse.creatorEntity = :creatorEntity and pse.productsEntity =:productsEntity and pse.currency = :currency and pse.settled = false")
    BigDecimal getPortoneChargesByCreatorAndProductsAndSettledIsFalse(
        @Param("creatorEntity") CreatorEntity creatorEntity,
        @Param("productsEntity") ProductsEntity productsEntity,
        @Param("currency") CurrencyType currency);
}
