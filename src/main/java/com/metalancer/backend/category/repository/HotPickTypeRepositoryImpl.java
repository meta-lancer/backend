package com.metalancer.backend.category.repository;

import com.metalancer.backend.admin.dto.AdminCategoryDTO.CategoryList;
import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.entity.HotPickTypeEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HotPickTypeRepositoryImpl implements HotPickTypeRepository {

    private final HotPickTypeJpaRepository hotPickTypeJpaRepository;

    @Override
    public List<CategoryList> getHotPickCategoryList() {
        return hotPickTypeJpaRepository.findAll().stream().map(HotPickTypeEntity::ToAdminCategory)
            .collect(
                Collectors.toList());
    }

    @Override
    public List<MainCategory> getHotPickCategoryListWithUseYnTrue() {
        return hotPickTypeJpaRepository.findAllByUseYnTrue().stream()
            .map(HotPickTypeEntity::ToMainCategory)
            .collect(
                Collectors.toList());
    }

    @Override
    public void updateCategoryUseYn(Long categoryId) {
        Optional<HotPickTypeEntity> optionalHotPickTypeEntity = hotPickTypeJpaRepository.findById(
            categoryId);
        optionalHotPickTypeEntity.ifPresent(
            HotPickTypeEntity::toggleUse);
    }
}
