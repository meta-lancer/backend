package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.Use_YN;
import com.metalancer.backend.products.entity.ProductsCategoryEntity;

public interface ProductsCategoryRepository {

    ProductsCategoryEntity findByCategoryNameAndUseYN(String categoryName, Use_YN useYn);

    ProductsCategoryEntity save(ProductsCategoryEntity newProductsCategoryEntity);
}
