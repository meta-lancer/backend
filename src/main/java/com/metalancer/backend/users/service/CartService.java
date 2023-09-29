package com.metalancer.backend.users.service;

import com.metalancer.backend.users.domain.Cart;
import com.metalancer.backend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartService {

    Page<Cart> getAllCart(User user, Pageable pageable);

    boolean toggleCart(User user, Long assetId);

    boolean deleteAllCart(User user);

    boolean createCart(User user, Long assetId);

    boolean deleteCart(User user, Long assetId);
}