package com.metalancer.backend.users.repository;

import com.metalancer.backend.products.entity.Products;
import com.metalancer.backend.users.domain.Cart;
import com.metalancer.backend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartRepository {

    Page<Cart> findAllByUser(User user, Pageable pageable);

    int countAllByUser(User user);

    boolean toggleCart(User user, Long assetId);

    void deleteAllCart(User user);

    Cart findCartById(User user, Long cartId);

    Cart findCartByUserAndAsset(User user, Products asset);

    int countCartCnt(User user);
}
