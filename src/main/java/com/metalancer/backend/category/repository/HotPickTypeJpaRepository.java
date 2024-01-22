package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.HotPickTypeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotPickTypeJpaRepository extends JpaRepository<HotPickTypeEntity, Long> {

    List<HotPickTypeEntity> findAllByUseYnTrue();
}
