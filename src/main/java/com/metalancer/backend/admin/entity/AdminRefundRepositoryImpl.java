package com.metalancer.backend.admin.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminRefundRepositoryImpl implements AdminRefundRepository {

    private final AdminRefundJpaRepository adminRefundJpaRepository;

    @Override
    public void save(AdminRefundEntity createdAdminRefundEntity) {
        adminRefundJpaRepository.save(createdAdminRefundEntity);
    }
}
