package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.ProductsSalesEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsSalesRepositoryImpl implements ProductsSalesRepository {

    private final ProductsSalesJpaRepository productsSalesJpaRepository;

    @Override
    public Integer getTotalPriceByCreatorAndDate(CreatorEntity creatorEntity, LocalDateTime date,
        LocalDateTime startOfNextDay) {
        return productsSalesJpaRepository.getTotalPriceByCreatorAndDate(creatorEntity, date,
            startOfNextDay);
    }

    @Override
    public int getSalesCntByCreatorAndDate(CreatorEntity creatorEntity, LocalDateTime date,
        LocalDateTime startOfNextDay) {
        return productsSalesJpaRepository.getSalesCntByCreatorAndDate(creatorEntity, date,
            startOfNextDay);
    }

    @Override
    public void save(ProductsSalesEntity createdProductsSalesEntity) {
        productsSalesJpaRepository.save(createdProductsSalesEntity);
    }
}
