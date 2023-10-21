package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreGalaxyTypeJpaRepository extends JpaRepository<GenreGalaxyTypeEntity, Long> {
    Optional<GenreGalaxyTypeEntity> findByName(String name);
}
