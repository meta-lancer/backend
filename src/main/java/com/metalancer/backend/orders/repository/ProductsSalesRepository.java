package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.orders.entity.ProductsSalesEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProductsSalesRepository {

    BigDecimal getTotalPriceByCreatorAndDate(CreatorEntity creatorEntity, LocalDateTime date,
        LocalDateTime startOfNextDay, CurrencyType currency);

    int getSalesCntByCreatorAndDate(CreatorEntity creatorEntity, LocalDateTime date,
        LocalDateTime startOfNextDay);

    BigDecimal getProductsTotalPriceByCreatorAndDate(CreatorEntity creatorEntity,
        ProductsEntity productsEntity, LocalDateTime date,
        LocalDateTime startOfNextDay, CurrencyType currency);

    int getProductsSalesCntByCreatorAndDate(CreatorEntity creatorEntity,
        ProductsEntity productsEntity, LocalDateTime date,
        LocalDateTime startOfNextDay);

    void save(ProductsSalesEntity createdProductsSalesEntity);

    int countAllByProducts(ProductsEntity productsEntity);

    BigDecimal getProductsTotalPriceByCreator(CreatorEntity creatorEntity,
        ProductsEntity productsEntity,
        CurrencyType currency);

    int countAllUnSettled(CreatorEntity creatorEntity);

    LocalDateTime getLastProductsSalesDate(CreatorEntity creatorEntity);
}
