package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.entity.HotPickTypeEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HotPickTypeRepositoryImpl implements HotPickTypeRepository {

    private final HotPickTypeJpaRepository hotPickTypeJpaRepository;

    @Override
    public List<MainCategory> getHotPickCategoryList() {
        return hotPickTypeJpaRepository.findAll().stream().map(HotPickTypeEntity::ToMainCategory)
            .collect(
                Collectors.toList());
    }
}
