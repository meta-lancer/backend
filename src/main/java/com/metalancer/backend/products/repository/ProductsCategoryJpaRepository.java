package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.Use_YN;
import com.metalancer.backend.products.entity.ProductsCategoryEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsCategoryJpaRepository extends JpaRepository<ProductsCategoryEntity, Long> {

    Optional<ProductsCategoryEntity> findByCategoryNameAndUseYn(String CategoryName, Use_YN useYn);
}
