package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.SettlementProductsEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SettlementProductsRepositoryImpl implements SettlementProductsRepository {

    private final SettlementProductsJpaRepository settlementProductsJpaRepository;

    @Override
    public int countAllByProducts(ProductsEntity productsEntity) {
        List<SettlementProductsEntity> settlementProductsEntityList = settlementProductsJpaRepository.findAllByProductsEntity(
            productsEntity);
        return settlementProductsEntityList.stream()
            .mapToInt(SettlementProductsEntity::getSalesQuantity)
            .sum();
    }
}
