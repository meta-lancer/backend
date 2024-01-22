package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.creators.entity.SettlementProductsEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SettlementProductsRepositoryImpl implements SettlementProductsRepository {

    private final SettlementProductsJpaRepository settlementProductsJpaRepository;

    @Override
    public int countAllByProducts(ProductsEntity productsEntity,
        List<SettlementStatus> settlementStatusList) {
        List<SettlementProductsEntity> settlementProductsEntityList = settlementProductsJpaRepository.findAllByProductsEntityAndSettlementStatusIn(
            productsEntity, settlementStatusList);
        return settlementProductsEntityList.stream()
            .mapToInt(SettlementProductsEntity::getSalesQuantity)
            .sum();
    }

    @Override
    public void save(SettlementProductsEntity settlementProductsEntity) {
        settlementProductsJpaRepository.save(settlementProductsEntity);
    }

    @Override
    public int countAllRemainByCreator(CreatorEntity creatorEntity) {
        int requestCnt = settlementProductsJpaRepository.findAllByCreatorEntityAndSettlementStatus(
            creatorEntity, SettlementStatus.REQUEST);
        int processCnt = settlementProductsJpaRepository.findAllByCreatorEntityAndSettlementStatus(
            creatorEntity, SettlementStatus.ING);
        return requestCnt + processCnt;
    }
}
