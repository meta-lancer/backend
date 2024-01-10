package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.creators.entity.ProductsSalesEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<ProductsSalesEntity> findAllByNotSettled(CreatorEntity creatorEntity, Pageable pageable);

    List<ProductsEntity> findAllProductsDistinctByCreatorEntityAndSettledIsFalse(
        CreatorEntity creatorEntity);

    List<ProductsSalesEntity> findAllByCreatorEntityAndSettledIsFalse(
        CreatorEntity creatorEntity);

    BigDecimal getTotalPriceByCreator(CreatorEntity creatorEntity, CurrencyType currencyType);

    BigDecimal getTotalPortoneChargesByCreator(CreatorEntity creatorEntity,
        CurrencyType currencyType);

    int countAllByProductsAndSettledIsFalse(ProductsEntity productsEntity);

    BigDecimal getTotalAmountByCreatorAndProductsAndSettledIsFalse(CreatorEntity creatorEntity,
        ProductsEntity productsEntity, CurrencyType currencyType);

    BigDecimal getPortoneChargesByCreatorAndProductsAndSettledIsFalse(CreatorEntity creatorEntity,
        ProductsEntity productsEntity, CurrencyType currencyType);
}
