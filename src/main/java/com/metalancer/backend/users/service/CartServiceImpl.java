package com.metalancer.backend.users.service;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.users.domain.Cart;
import com.metalancer.backend.users.entity.CartEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.CartRepository;
import com.metalancer.backend.users.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductsRepository productsRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Cart> getAllCart(User user, Pageable pageable) {
        return cartRepository.findAllByUser(user, pageable);
    }

    @Override
    public boolean deleteAllCart(User user) {
        cartRepository.deleteAllCart(user);
        return cartRepository.countCartCnt(user) == 0;
    }

    @Override
    public boolean createCart(User user, Long assetId) {
        if (user == null) {
            user = userRepository.findById(1L).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND)
            );
        }
        ProductsEntity foundProductsEntity = productsRepository.findProductById(assetId);
        Optional<CartEntity> cartEntity = cartRepository.findCartByUserAndAsset(user,
            foundProductsEntity);
        if (cartEntity.isEmpty()) {
            cartRepository.createCart(user, foundProductsEntity);
            return true;
        }
        if (cartEntity.get().getStatus().equals(DataStatus.DELETED)) {
            cartEntity.get().restoreCart();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCart(User user, Long assetId) {
        if (user == null) {
            user = userRepository.findById(1L).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND)
            );
        }
        ProductsEntity foundProductsEntity = productsRepository.findProductById(assetId);
        Optional<CartEntity> cartEntity = cartRepository.findCartByUserAndAsset(user,
            foundProductsEntity);
        if (cartEntity.isPresent() && cartEntity.get().getStatus().equals(DataStatus.ACTIVE)) {
            cartEntity.get().deleteCart();
            return true;
        }
        return false;
    }
}