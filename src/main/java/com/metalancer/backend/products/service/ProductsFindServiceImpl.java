package com.metalancer.backend.products.service;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.domain.Products;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsFindServiceImpl implements ProductsFindService {

    private final ProductsRepository productsRepository;
    private final String product = "상품";

    @Override
    public Products findProductsById(Long productsId) {
        return productsRepository.findById(productsId).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
    }

    @Override
    public Page<Products> findProductsListByCreator(User creator, Pageable pageable) {
        return productsRepository.findAllByCreator(creator, pageable);
    }

    @Override
    public Page<Products> findProductsListByCreatorAndStatus(User creator, DataStatus status,
        Pageable pageable) {
        return productsRepository.findAllByCreatorAndStatus(creator, status, pageable);
    }
}