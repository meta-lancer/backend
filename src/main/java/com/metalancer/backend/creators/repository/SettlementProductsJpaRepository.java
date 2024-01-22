package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.creators.entity.SettlementProductsEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementProductsJpaRepository extends
    JpaRepository<SettlementProductsEntity, Long> {

    List<SettlementProductsEntity> findAllByProductsEntityAndSettlementStatusIn(
        ProductsEntity productsEntity, List<SettlementStatus> settlementStatus);

    int findAllByCreatorEntityAndSettlementStatus(CreatorEntity creatorEntity,
        SettlementStatus settlementStatus);
}
