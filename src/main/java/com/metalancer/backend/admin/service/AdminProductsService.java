package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.ProductsList;
import com.metalancer.backend.products.domain.ProductsDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminProductsService {


    Page<ProductsList> getAdminProductsList(Pageable pageable);

    ProductsDetail getProductDetail(Long productId);
}