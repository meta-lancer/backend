package com.metalancer.backend.request.repository;

import com.metalancer.backend.request.domain.ProductsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductsRequestRepository {

    Page<ProductsRequest> findAll(List<String> requestTypeOptions, Pageable adjustedPageable);
}