package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GenreGalaxyTypeRepositoryImpl implements GenreGalaxyTypeRepository {

    private final GenreGalaxyTypeJpaRepository genreGalaxyTypeJpaRepository;

    @Override
    public List<MainCategory> getGenreGalaxyCategoryList() {
        return genreGalaxyTypeJpaRepository.findAll().stream()
            .map(GenreGalaxyTypeEntity::ToMainCategory)
            .collect(
                Collectors.toList());
    }
}