package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsWishEntity;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsWishJpaRepository extends JpaRepository<ProductsWishEntity, Long> {

    Optional<ProductsWishEntity> findByUserAndProductsEntityAndStatus(User user,
        ProductsEntity productsEntity, DataStatus status);
}
