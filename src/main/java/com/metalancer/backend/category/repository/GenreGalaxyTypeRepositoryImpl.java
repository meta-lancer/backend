package com.metalancer.backend.category.repository;

import com.metalancer.backend.admin.dto.AdminCategoryDTO.CategoryList;
import com.metalancer.backend.admin.dto.AdminCategoryDTO.CreateCategory;
import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GenreGalaxyTypeRepositoryImpl implements GenreGalaxyTypeRepository {

    private final GenreGalaxyTypeJpaRepository genreGalaxyTypeJpaRepository;

    @Override
    public List<CategoryList> getGenreGalaxyCategoryList() {
        return genreGalaxyTypeJpaRepository.findAll().stream()
            .map(GenreGalaxyTypeEntity::ToAdminCategory)
            .collect(
                Collectors.toList());
    }

    @Override
    public List<MainCategory> getGenreGalaxyCategoryListWithUseYnTrue() {
        return genreGalaxyTypeJpaRepository.findAllByUseYnTrue().stream()
            .map(GenreGalaxyTypeEntity::ToMainCategory)
            .collect(
                Collectors.toList());
    }


    @Override
    public GenreGalaxyTypeEntity findByName(String type) {
        return genreGalaxyTypeJpaRepository.findByName(type).orElseThrow(
            () -> new NotFoundException("GenreGalaxy: ", ErrorCode.TYPE_NOT_FOUND)
        );
    }

    @Override
    public List<MainCategory> getGenreGalaxyCategoryListWithOutAll() {
        return genreGalaxyTypeJpaRepository.findAllByTagsEntityIsNotNull().stream()
            .map(GenreGalaxyTypeEntity::ToMainCategory)
            .collect(
                Collectors.toList());
    }

    @Override
    public void updateCategoryUseYn(Long categoryId) {
        Optional<GenreGalaxyTypeEntity> optionalGenreGalaxyTypeEntity = genreGalaxyTypeJpaRepository.findById(
            categoryId);
        optionalGenreGalaxyTypeEntity.ifPresent(
            GenreGalaxyTypeEntity::toggleUse);
    }

    @Override
    public void createCategory(CreateCategory dto) {
        GenreGalaxyTypeEntity createdGenreGalaxyTypeEntity = GenreGalaxyTypeEntity.builder()
            .nameKor(
                dto.getNameKor()).name(dto.getName()).build();
        genreGalaxyTypeJpaRepository.save(createdGenreGalaxyTypeEntity);
    }
}
