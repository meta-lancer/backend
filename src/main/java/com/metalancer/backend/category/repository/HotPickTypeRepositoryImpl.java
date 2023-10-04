package com.metalancer.backend.category.repository;

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
    public List<String> getHotPickCategoryList() {
        return hotPickTypeJpaRepository.findAll().stream().map(HotPickTypeEntity::getName).collect(
            Collectors.toList());
    }
}
