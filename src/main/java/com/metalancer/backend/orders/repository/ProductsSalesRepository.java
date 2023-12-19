package com.metalancer.backend.orders.repository;

import com.metalancer.backend.users.entity.CreatorEntity;
import java.time.LocalDateTime;

public interface ProductsSalesRepository {

    Integer getTotalPriceByCreatorAndDate(CreatorEntity creatorEntity, LocalDateTime date,
        LocalDateTime startOfNextDay);

    int getSalesCntByCreatorAndDate(CreatorEntity creatorEntity, LocalDateTime date,
        LocalDateTime startOfNextDay);
}
