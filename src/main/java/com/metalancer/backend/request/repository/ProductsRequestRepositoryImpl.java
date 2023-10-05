package com.metalancer.backend.request.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.request.domain.ProductsRequest;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRequestRepositoryImpl implements ProductsRequestRepository {

    private final ProductsRequestJpaRepository productsRequestJpaRepository;

    @Override
    public Page<ProductsRequest> findAll(Pageable adjustedPageable) {
        Page<ProductsRequestEntity> productsRequests = productsRequestJpaRepository.findAllByStatus(
            DataStatus.ACTIVE, adjustedPageable);
        return productsRequests.map(ProductsRequestEntity::toDomain);
    }
}
