package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GenreGalaxyTypeJpaRepository extends JpaRepository<GenreGalaxyTypeEntity, Long> {

    @Query("select ggt from genre_galaxy_type ggt where ggt.tagsEntity.tagNameEn = :name")
    Optional<GenreGalaxyTypeEntity> findByName(@Param("name") String name);
}
