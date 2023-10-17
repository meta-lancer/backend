package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsWishEntity;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsWishRepositoryImpl implements ProductsWishRepository {

    private final ProductsWishJpaRepository productsWishJpaRepository;

    @Override
    public Optional<ProductsWishEntity> findByUserAndProduct(User foundUser,
        ProductsEntity foundProductsEntity) {
        return productsWishJpaRepository.findByUserAndProductsEntityAndStatus(foundUser,
            foundProductsEntity, DataStatus.ACTIVE);
    }
}
