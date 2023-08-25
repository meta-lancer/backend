package com.metalancer.backend.products.service;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.domain.Products;
import com.metalancer.backend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductsFindService {

    Products findProductsById(Long productsId);

    Page<Products> findProductsListByCreator(User creator, Pageable pageable);
    
    Page<Products> findProductsListByCreatorAndStatus(User creator, DataStatus status,
        Pageable pageable);
}