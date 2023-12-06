package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.entity.PayedAssetsEntity;
import com.metalancer.backend.users.entity.User;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PayedAssetsRepository {

    void save(PayedAssetsEntity createdPayedAssetsEntity);

    Page<PayedAssets> findAllPayedAssetListWithStatusAndDateOption(User foundUser,
        Pageable pageable, LocalDateTime beginAt, LocalDateTime endAt, OrderStatus orderStatus);

    Page<PayedAssets> findAllPayedAssetListWithStatusAndDateOption(User foundUser,
        Pageable pageable, LocalDateTime beginAt, LocalDateTime endAt);
}
