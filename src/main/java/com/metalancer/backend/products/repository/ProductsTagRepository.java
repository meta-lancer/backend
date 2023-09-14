package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsTagEntity;
import java.util.List;

public interface ProductsTagRepository {

    List<String> findTagListByProduct(ProductsEntity products);

    public List<ProductsTagEntity> saveAll(List<ProductsTagEntity> productsTagEntityList);
}
