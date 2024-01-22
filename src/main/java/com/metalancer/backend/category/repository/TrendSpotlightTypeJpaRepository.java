package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrendSpotlightTypeJpaRepository extends
    JpaRepository<TrendSpotlightTypeEntity, Long> {

    List<TrendSpotlightTypeEntity> findAllByUseYnTrue();

    @Query("select tsl from trend_spotlight_type tsl where tsl.tagsEntity.tagNameEn = :name")
    Optional<TrendSpotlightTypeEntity> findByName(@Param("name") String name);
}
