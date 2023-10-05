package com.metalancer.backend.request.service;

import com.metalancer.backend.request.domain.ProductsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestService {

    Page<ProductsRequest> getProductsRequestList(Pageable adjustedPageable);
}