package com.metalancer.backend.admin.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminRefundProductsRepositoryImpl implements AdminRefundProductsRepository {

    private final AdminRefundProductsJpaRepository adminRefundProductsJpaRepository;

    @Override
    public void save(AdminRefundProductsEntity createdAdminRefundProductsEntity) {
        adminRefundProductsJpaRepository.save(createdAdminRefundProductsEntity);
    }
}
