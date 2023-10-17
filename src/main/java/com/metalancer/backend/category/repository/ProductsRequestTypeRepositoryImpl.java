package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import com.metalancer.backend.category.entity.ProductsRequestTypeEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRequestTypeRepositoryImpl implements ProductsRequestTypeRepository {

    private final ProductsRequestTypeJpaRepository productsRequestTypeJpaRepository;

    @Override
    public List<RequestCategory> getRequestCategoryList() {
        return productsRequestTypeJpaRepository.findAll().stream()
            .map(ProductsRequestTypeEntity::ToRequestCategory).collect(Collectors.toList());
    }
}
