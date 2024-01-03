package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.domain.RequestOption;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsRequestOptionEntity;
import java.util.List;
import java.util.Optional;

public interface ProductsRequestOptionRepository {

    List<RequestOption> findAllByProducts(ProductsEntity foundProductsEntity);

    Optional<ProductsRequestOptionEntity> findOptionById(Long requestOptionId);
}
