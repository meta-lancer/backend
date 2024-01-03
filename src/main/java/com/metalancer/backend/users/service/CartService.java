package com.metalancer.backend.users.service;

import com.metalancer.backend.users.domain.Cart;
import com.metalancer.backend.users.dto.UserRequestDTO.CreateCartRequest;
import com.metalancer.backend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartService {

    Page<Cart> getAllCart(User user, Pageable pageable);

    boolean deleteAllCart(User user);

    boolean createCart(User user, CreateCartRequest assetId);

    boolean deleteCart(User user, Long assetId);
}