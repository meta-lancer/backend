package com.metalancer.backend.users.service;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.users.domain.Cart;
import com.metalancer.backend.users.entity.CartEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.CartRepository;
import java.util.Optional;
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
    private final ProductsRepository productsRepository;

    @Override
    public Page<Cart> getAllCart(User user, Pageable pageable) {
        return cartRepository.findAllByUser(user, pageable);
    }

    @Override
    public boolean toggleCart(User user, Long assetId) {
        ProductsEntity foundProductsEntity = productsRepository.findProductById(assetId);
        Optional<CartEntity> cartEntity = cartRepository.findCartByUserAndAsset(user,
            foundProductsEntity);
        boolean result = false;
        if (cartEntity.isEmpty()) {
            cartRepository.createCart(user, foundProductsEntity);
            result = true;
        } else {
            CartEntity foundCartEntity = cartEntity.get();
            if (foundCartEntity.getStatus().equals(DataStatus.DELETED)) {
                foundCartEntity.restore();
                result = true;
            } else {
                if (foundCartEntity.getStatus().equals(DataStatus.ACTIVE)) {
                    foundCartEntity.delete();
                }
            }
        }
        return result;
    }

    @Override
    public boolean deleteAllCart(User user) {
        cartRepository.deleteAllCart(user);
        return cartRepository.countCartCnt(user) == 0;
    }

    @Override
    public boolean createCart(User user, Long assetId) {
        ProductsEntity foundProductsEntity = productsRepository.findProductById(assetId);
        Optional<CartEntity> cartEntity = cartRepository.findCartByUserAndAsset(user,
            foundProductsEntity);
        if (cartEntity.isEmpty()) {
            cartRepository.createCart(user, foundProductsEntity);
            return true;
        }
        if (cartEntity.get().getStatus().equals(DataStatus.DELETED)) {
            cartEntity.get().restore();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCart(User user, Long assetId) {
        ProductsEntity foundProductsEntity = productsRepository.findProductById(assetId);
        Optional<CartEntity> cartEntity = cartRepository.findCartByUserAndAsset(user,
            foundProductsEntity);
        if (cartEntity.isPresent() && cartEntity.get().getStatus().equals(DataStatus.ACTIVE)) {
            cartEntity.get().delete();
            return true;
        }
        return false;
    }
}