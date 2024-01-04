package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.orders.entity.ProductsSalesEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductsSalesJpaRepository extends JpaRepository<ProductsSalesEntity, Long> {

    @Query("select SUM(pse.price) from product_sales pse where pse.creatorEntity = :creatorEntity and pse.createdAt between :startDate and :startOfNextDay and pse.currency = :currency")
    Integer getTotalPriceByCreatorAndDate(@Param("creatorEntity") CreatorEntity creatorEntity,
        @Param("startDate") LocalDateTime startDate,
        @Param("startOfNextDay") LocalDateTime startOfNextDay,
        @Param("currency") CurrencyType currency
    );

    @Query("select count(pse) from product_sales pse where pse.creatorEntity = :creatorEntity and pse.createdAt between :startDate and :startOfNextDay")
    int getSalesCntByCreatorAndDate(@Param("creatorEntity") CreatorEntity creatorEntity,
        @Param("startDate") LocalDateTime startDate,
        @Param("startOfNextDay") LocalDateTime startOfNextDay);
}
