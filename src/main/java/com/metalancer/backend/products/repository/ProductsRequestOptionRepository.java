package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.domain.RequestOption;
import com.metalancer.backend.products.entity.ProductsEntity;
import java.util.List;

public interface ProductsRequestOptionRepository {

    List<RequestOption> findAllByProducts(ProductsEntity foundProductsEntity);
}
