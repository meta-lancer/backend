package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsViewsEntity;
import java.util.List;

public interface ProductsViewsRepository {

    void saveAll(List<ProductsViewsEntity> productsViewsEntities);

    List<String> findAllUrlByProduct(ProductsEntity savedProductsEntity);
}
