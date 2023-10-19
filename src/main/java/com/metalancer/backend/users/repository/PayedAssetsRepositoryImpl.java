package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.entity.PayedAssetsEntity;
import com.metalancer.backend.users.entity.User;
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
    public Page<PayedAssets> findAllPayedAssetList(User user, Pageable pageable) {
        Page<PayedAssetsEntity> payedAssetsEntities = payedAssetsJpaRepository.findAllByUserAndStatusOrderByCreatedAtDesc(
            user, DataStatus.ACTIVE,
            pageable);
        return payedAssetsEntities.map(PayedAssetsEntity::toDomain);
    }


}
