package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsRequestOptionEntity;
import com.metalancer.backend.users.entity.CartEntity;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {

    Page<CartEntity> findAllByUser(User user, Pageable pageable);

    int countAllByUser(User user);

    int countAllByProducts(ProductsEntity products);

    void deleteAllByUser(User user);

    Optional<CartEntity> findByUserAndProducts(User user, ProductsEntity products);

    Optional<CartEntity> findByUserAndProductsAndProductsRequestOptionEntity(User user,
        ProductsEntity products, ProductsRequestOptionEntity productsRequestOptionEntity);

    Optional<CartEntity> findByUserAndProductsAndStatus(User user, ProductsEntity products,
        DataStatus status);

    Optional<CartEntity> findByIdAndUser(Long id, User user);
}
