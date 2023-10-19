package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.entity.PayedAssetsEntity;
import com.metalancer.backend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PayedAssetsRepository {

    void save(PayedAssetsEntity createdPayedAssetsEntity);

    Page<PayedAssets> findAllPayedAssetList(User user, Pageable pageable);

}
