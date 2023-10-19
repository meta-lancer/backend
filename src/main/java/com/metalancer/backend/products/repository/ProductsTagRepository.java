package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsTagEntity;
import java.util.List;

public interface ProductsTagRepository {

    List<String> findTagListByProduct(ProductsEntity products);

    List<ProductsTagEntity> findTagEntityListByProduct(ProductsEntity products);

    public List<ProductsTagEntity> saveAll(List<ProductsTagEntity> productsTagEntityList);

    void deleteAll(List<ProductsTagEntity> productsTagEntities);
}
