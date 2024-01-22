package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.creators.entity.SettlementEntity;
import com.metalancer.backend.creators.entity.SettlementProductsEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.List;

public interface SettlementProductsRepository {

    int countAllByProducts(ProductsEntity productsEntity, List<SettlementStatus> settlementStatus);

    void save(SettlementProductsEntity settlementProductsEntity);

    int countAllRemainByCreator(CreatorEntity creatorEntity);

    Integer countAllBySettlement(SettlementEntity settlementEntity);
}
