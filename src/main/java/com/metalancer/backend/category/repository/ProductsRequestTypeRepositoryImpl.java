package com.metalancer.backend.category.repository;

import com.metalancer.backend.admin.dto.AdminCategoryDTO.CategoryList;
import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import com.metalancer.backend.category.entity.ProductsRequestTypeEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRequestTypeRepositoryImpl implements ProductsRequestTypeRepository {

    private final ProductsRequestTypeJpaRepository productsRequestTypeJpaRepository;

    @Override
    public List<CategoryList> getRequestCategoryList() {
        return productsRequestTypeJpaRepository.findAll().stream()
            .map(ProductsRequestTypeEntity::ToAdminCategory).collect(Collectors.toList());
    }

    @Override
    public List<RequestCategory> getRequestCategoryListWithUseYn() {
        return productsRequestTypeJpaRepository.findAllByUseYnTrue().stream()
            .map(ProductsRequestTypeEntity::ToRequestCategory).collect(Collectors.toList());
    }

    @Override
    public void updateCategoryUseYn(Long categoryId) {
        Optional<ProductsRequestTypeEntity> optionalProductsRequestTypeEntity = productsRequestTypeJpaRepository.findById(
            categoryId);
        optionalProductsRequestTypeEntity.ifPresent(
            ProductsRequestTypeEntity::toggleUseYn);
    }
}
