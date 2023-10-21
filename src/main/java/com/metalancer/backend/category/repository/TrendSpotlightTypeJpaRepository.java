package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrendSpotlightTypeJpaRepository extends
        JpaRepository<TrendSpotlightTypeEntity, Long> {
    Optional<TrendSpotlightTypeEntity> findByName(String name);
}
