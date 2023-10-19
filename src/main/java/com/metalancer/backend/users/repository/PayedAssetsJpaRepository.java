package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.users.entity.PayedAssetsEntity;
import com.metalancer.backend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayedAssetsJpaRepository extends JpaRepository<PayedAssetsEntity, Long> {

    Page<PayedAssetsEntity> findAllByUserAndStatusOrderByCreatedAtDesc(User user, DataStatus status,
        Pageable pageable);

}
