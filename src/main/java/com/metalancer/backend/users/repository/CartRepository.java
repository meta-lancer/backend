package com.metalancer.backend.users.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsRequestOptionEntity;
import com.metalancer.backend.users.domain.Cart;
import com.metalancer.backend.users.entity.CartEntity;
import com.metalancer.backend.users.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartRepository {

    Page<Cart> findAllByUser(User user, Pageable pageable);

    int countAllByUser(User user);

    int countAllByProducts(ProductsEntity productsEntity);

    void createCart(User user, ProductsEntity foundProductsEntity);

    void deleteAllCart(User user);

    Cart findCartById(User user, Long cartId);

    Optional<CartEntity> findCartByUserAndAsset(User user, ProductsEntity asset);

    List<CartEntity> findAllCartByUserAndAsset(User user, ProductsEntity asset);

    int countCartCnt(User user);

    void deleteCart(User user, ProductsEntity productsEntity,
        ProductsRequestOptionEntity productsRequestOptionEntity);

    Optional<CartEntity> findCartByUserAndAssetAndOption(User user,
        ProductsEntity foundProductsEntity,
        ProductsRequestOptionEntity productsRequestOptionEntity);

    void createCartWithOption(User user, ProductsEntity foundProductsEntity,
        ProductsRequestOptionEntity productsRequestOptionEntity);
}
