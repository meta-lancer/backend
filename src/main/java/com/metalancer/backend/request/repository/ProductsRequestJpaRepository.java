package com.metalancer.backend.request.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRequestJpaRepository extends JpaRepository<ProductsRequestEntity, Long> {

    Page<ProductsRequestEntity> findAllByStatus(DataStatus status, Pageable pageable);
}
