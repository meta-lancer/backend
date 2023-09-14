package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.Use_YN;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.products.entity.ProductsCategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsCategoryRepositoryImpl implements ProductsCategoryRepository {

    private final ProductsCategoryJpaRepository productsCategoryJpaRepository;

    @Override
    public ProductsCategoryEntity findByCategoryNameAndUseYN(String categoryName, Use_YN useYn) {
        return productsCategoryJpaRepository.findByCategoryNameAndUseYn(categoryName, useYn)
            .orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND)
            );
    }

    @Override
    public ProductsCategoryEntity save(ProductsCategoryEntity newProductsCategoryEntity) {
        return productsCategoryJpaRepository.save(newProductsCategoryEntity);
    }
}
