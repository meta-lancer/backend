package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.entity.PayedAssetsEntity;
import com.metalancer.backend.users.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PayedAssetsRepositoryImpl implements PayedAssetsRepository {

    private final PayedAssetsJpaRepository payedAssetsJpaRepository;

    @Override
    public void save(PayedAssetsEntity createdPayedAssetsEntity) {
        payedAssetsJpaRepository.save(createdPayedAssetsEntity);
    }

    @Override
    public Page<PayedAssets> findAllPayedAssetListWithStatusAndDateOption(User user,
        Pageable pageable, LocalDateTime beginAt, LocalDateTime endAt, OrderStatus orderStatus) {
        LocalDateTime adjustedEndAt = endAt.plusDays(1);
        Page<PayedAssetsEntity> payedAssetsEntities = payedAssetsJpaRepository.findAllByUser(
            beginAt, adjustedEndAt,
            user, orderStatus,
            pageable);
        return payedAssetsEntities.map(PayedAssetsEntity::toDomain);
    }

    @Override
    public Page<PayedAssets> findAllPayedAssetListWithStatusAndDateOption(User foundUser,
        Pageable pageable, LocalDateTime beginAt, LocalDateTime endAt) {
        LocalDateTime adjustedEndAt = endAt.plusDays(1);
        Page<PayedAssetsEntity> payedAssetsEntities = payedAssetsJpaRepository.findAllByUser(
            beginAt, adjustedEndAt,
            foundUser, pageable);
        return payedAssetsEntities.map(PayedAssetsEntity::toDomain);
    }

    @Override
    public Optional<PayedAssetsEntity> findByUserAndProductsAndStatus(User user,
        ProductsEntity productsEntity, DataStatus status) {
        return payedAssetsJpaRepository.findByUserAndProductsAndStatus(user, productsEntity,
            status);
    }

    @Override
    public List<PayedAssetsEntity> findAllByUserAndProductsAndStatus(User user,
        ProductsEntity productsEntity, DataStatus status) {
        return payedAssetsJpaRepository.findAllByUserAndProductsAndStatus(user, productsEntity,
            status);
    }

}
