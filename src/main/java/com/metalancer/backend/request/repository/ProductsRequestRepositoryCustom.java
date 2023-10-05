package com.metalancer.backend.request.repository;

import com.metalancer.backend.request.domain.ProductsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRequestRepositoryCustom {
    Page<ProductsRequest> findAll(List<String> requestTypeOptions, Pageable adjustedPageable);
}
