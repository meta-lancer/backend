package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsWishEntity;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;

public interface ProductsWishRepository {

    Optional<ProductsWishEntity> findByUserAndProduct(User foundUser,
        ProductsEntity foundProductsEntity);

    int countAllByUserAndProduct(User foundUser,
        ProductsEntity foundProductsEntity);
}
