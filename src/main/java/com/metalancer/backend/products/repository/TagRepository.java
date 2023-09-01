package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import java.util.List;

public interface TagRepository {

    List<String> findTagListByProduct(ProductsEntity products);
}
