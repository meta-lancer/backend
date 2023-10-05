package com.metalancer.backend.request.service;

import com.metalancer.backend.request.domain.ProductsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestService {

    Page<ProductsRequest> getProductsRequestList(List<String> requestTypeOptions,
                                                 Pageable adjustedPageable);
}