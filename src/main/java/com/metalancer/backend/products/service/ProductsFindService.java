package com.metalancer.backend.products.service;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.domain.Products;
import org.springframework.data.domain.Page;

public interface ProductsFindService {

    Products findProductsById(Long productsId);

    Page<Products> findProductsListByCreator(Long creatorId);

    Products findProductsByIdAndStatus(Long productsId, DataStatus status);

    Page<Products> findProductsListByCreatorAndStatus(Long creatorId, DataStatus status);
}