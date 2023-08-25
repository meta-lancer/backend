package com.metalancer.backend.products.service;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.domain.Products;
import com.metalancer.backend.products.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsFindServiceImpl implements ProductsFindService {

    private final ProductsRepository productsRepository;

    @Override
    public Products findProductsById(Long productsId) {
        return null;
    }

    @Override
    public Page<Products> findProductsListByCreator(Long creatorId) {
        return null;
    }

    @Override
    public Products findProductsByIdAndStatus(Long productsId, DataStatus status) {
        return null;
    }

    @Override
    public Page<Products> findProductsListByCreatorAndStatus(Long creatorId, DataStatus status) {
        return null;
    }
}