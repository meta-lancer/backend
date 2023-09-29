package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.domain.Cart;
import com.metalancer.backend.users.entity.CartEntity;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

    private final CartJpaRepository cartJpaRepository;

    @Override
    public Page<Cart> findAllByUser(User user, Pageable pageable) {
        return cartJpaRepository.findAllByUser(user, pageable).map(CartEntity::toDomain);
    }

    @Override
    public int countAllByUser(User user) {
        return countCartCnt(user);
    }

    @Override
    public boolean createCart(User user, ProductsEntity foundProductsEntity) {
        CartEntity savedCartEntity = CartEntity.builder().user(user).products(foundProductsEntity)
            .build();
        cartJpaRepository.save(savedCartEntity);
        return cartJpaRepository.findById(savedCartEntity.getId()).isPresent();
    }

    @Override
    public void deleteAllCart(User user) {
        cartJpaRepository.deleteAllByUser(user);
    }

    @Override
    public Cart findCartById(User user, Long cartId) {
        return cartJpaRepository.findByIdAndUser(cartId, user).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        ).toDomain();
    }

    @Override
    public Optional<CartEntity> findCartByUserAndAsset(User user, ProductsEntity asset) {
        return cartJpaRepository.findByUserAndProducts(user, asset);
    }

    @Override
    public int countCartCnt(User user) {
        return cartJpaRepository.countAllByUser(user);
    }

    @Override
    public void deleteCart(User user, ProductsEntity productsEntity) {
        Optional<CartEntity> foundCartEntity = cartJpaRepository.findByUserAndProductsAndStatus(
            user, productsEntity, DataStatus.ACTIVE);
        foundCartEntity.ifPresent(CartEntity::delete);
    }
}
