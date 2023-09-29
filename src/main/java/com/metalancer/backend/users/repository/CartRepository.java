package com.metalancer.backend.users.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.domain.Cart;
import com.metalancer.backend.users.entity.CartEntity;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartRepository {

    Page<Cart> findAllByUser(User user, Pageable pageable);

    int countAllByUser(User user);

    boolean createCart(User user, ProductsEntity foundProductsEntity);

    void deleteAllCart(User user);

    Cart findCartById(User user, Long cartId);

    Optional<CartEntity> findCartByUserAndAsset(User user, ProductsEntity asset);

    int countCartCnt(User user);

    void deleteCart(User user, ProductsEntity productsEntity);
}
