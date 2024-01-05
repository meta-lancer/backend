package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.orders.entity.ProductsSalesEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
}
