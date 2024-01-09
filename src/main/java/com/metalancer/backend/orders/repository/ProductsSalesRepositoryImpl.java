package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.orders.entity.ProductsSalesEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsSalesRepositoryImpl implements ProductsSalesRepository {

    private final ProductsSalesJpaRepository productsSalesJpaRepository;

    @Override
    public BigDecimal getTotalPriceByCreatorAndDate(CreatorEntity creatorEntity, LocalDateTime date,
        LocalDateTime startOfNextDay, CurrencyType currency) {
        return productsSalesJpaRepository.getTotalPriceByCreatorAndDate(creatorEntity, date,
            startOfNextDay, currency);
    }

    @Override
    public int getSalesCntByCreatorAndDate(CreatorEntity creatorEntity, LocalDateTime date,
        LocalDateTime startOfNextDay) {
        return productsSalesJpaRepository.getSalesCntByCreatorAndDate(creatorEntity, date,
            startOfNextDay);
    }

    @Override
    public BigDecimal getProductsTotalPriceByCreatorAndDate(CreatorEntity creatorEntity,
        ProductsEntity productsEntity, LocalDateTime date, LocalDateTime startOfNextDay,
        CurrencyType currency) {
        return productsSalesJpaRepository.getProductsTotalPriceByCreatorAndDate(creatorEntity,
            productsEntity, date,
            startOfNextDay, currency);
    }

    @Override
    public int getProductsSalesCntByCreatorAndDate(CreatorEntity creatorEntity,
        ProductsEntity productsEntity, LocalDateTime date, LocalDateTime startOfNextDay) {
        return productsSalesJpaRepository.getProductsSalesCntByCreatorAndDate(creatorEntity,
            productsEntity, date,
            startOfNextDay);
    }

    @Override
    public void save(ProductsSalesEntity createdProductsSalesEntity) {
        productsSalesJpaRepository.save(createdProductsSalesEntity);
    }

    @Override
    public int countAllByProducts(ProductsEntity productsEntity) {
        return productsSalesJpaRepository.countAllByProductsEntity(productsEntity);
    }

    @Override
    public BigDecimal getProductsTotalPriceByCreator(CreatorEntity creatorEntity,
        ProductsEntity productsEntity,
        CurrencyType currency) {
        return productsSalesJpaRepository.getProductsTotalPriceByCreator(creatorEntity,
            productsEntity, currency);
    }

    @Override
    public int countAllUnSettled(CreatorEntity creatorEntity) {
        return productsSalesJpaRepository.countAllByCreatorEntityAndSettledIsFalse(creatorEntity);
    }

    @Override
    public LocalDateTime getLastProductsSalesDate(CreatorEntity creatorEntity) {
        return productsSalesJpaRepository.findFirstByCreatorEntityOrderByCreatedAtDesc(
            creatorEntity).map(
            ProductsSalesEntity::getCreatedAt).orElse(null);
    }

    @Override
    public Page<ProductsSalesEntity> findAllByNotSettled(CreatorEntity creatorEntity,
        Pageable pageable) {
        return productsSalesJpaRepository.findAllByCreatorEntityAndSettledIsFalse(creatorEntity,
            pageable);
    }
}
