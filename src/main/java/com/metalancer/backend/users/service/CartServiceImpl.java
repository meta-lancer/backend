package com.metalancer.backend.users.service;

import com.metalancer.backend.users.domain.Cart;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public Page<Cart> getAllCart(User user, Pageable pageable) {
        return cartRepository.findAllByUser(user, pageable);
    }

    @Override
    public boolean toggleCart(User user, Long assetId) {
        return false;
    }

    @Override
    public boolean deleteAllCart(User user) {
        cartRepository.deleteAllCart(user);
        return cartRepository.countCartCnt(user) == 0;
    }
}