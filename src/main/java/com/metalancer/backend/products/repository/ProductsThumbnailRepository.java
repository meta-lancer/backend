package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsThumbnailEntity;
import java.util.List;

public interface ProductsThumbnailRepository {

    void saveAll(List<ProductsThumbnailEntity> productsThumbnailEntities);

    List<String> findAllUrlByProduct(ProductsEntity savedProductsEntity);
}
