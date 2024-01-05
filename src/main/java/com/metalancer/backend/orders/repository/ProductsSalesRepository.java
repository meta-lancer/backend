package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.orders.entity.ProductsSalesEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProductsSalesRepository {

    BigDecimal getTotalPriceByCreatorAndDate(CreatorEntity creatorEntity, LocalDateTime date,
        LocalDateTime startOfNextDay, CurrencyType currency);

    int getSalesCntByCreatorAndDate(CreatorEntity creatorEntity, LocalDateTime date,
        LocalDateTime startOfNextDay);

    void save(ProductsSalesEntity createdProductsSalesEntity);
}
