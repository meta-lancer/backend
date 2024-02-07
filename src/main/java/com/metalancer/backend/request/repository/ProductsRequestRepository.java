package com.metalancer.backend.request.repository;

import com.metalancer.backend.request.domain.ProductsRequest;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Create;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Update;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import com.metalancer.backend.users.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductsRequestRepository {

    Page<ProductsRequest> findAll(List<String> requestTypeOptions, Pageable adjustedPageable);

    ProductsRequest findDetail(Long requestId);

    ProductsRequestEntity findEntity(Long requestId);

    ProductsRequest save(User user, Create dto);

    ProductsRequest update(User user, Update dto, ProductsRequestEntity productsRequestEntity);

    Page<ProductsRequest> findAllByUser(User foundUser, Pageable pageable);
}
