package com.metalancer.backend.request.service;

import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.request.domain.ProductsRequest;
import com.metalancer.backend.request.repository.ProductsRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class RequestServiceImpl implements RequestService {

    private final ProductsRequestRepository productsRequestRepository;

    @Override
    public Page<ProductsRequest> getProductsRequestList(Pageable adjustedPageable) {
        return productsRequestRepository.findAll(adjustedPageable);
    }
}