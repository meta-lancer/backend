package com.metalancer.backend.request.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import com.metalancer.backend.users.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductsRequestJpaRepository extends JpaRepository<ProductsRequestEntity, Long> {

    Page<ProductsRequestEntity> findAllByStatusOrderByCreatedAtDesc(DataStatus status,
        Pageable pageable);

    Optional<ProductsRequestEntity> findProductsRequestEntityByIdAndStatus(Long id,
        DataStatus status);

    @Query("select DISTINCT prat.productsRequestEntity from products_request_and_type prat where prat.productsRequestTypeEn in :requestTypeOptions and prat.productsRequestEntity.status = 'ACTIVE' order by prat.productsRequestEntity.createdAt DESC ")
    Page<ProductsRequestEntity> findAllByRequestTypeOptions(
        @Param("requestTypeOptions") List<String> requestTypeOptions,
        Pageable pageable);

    Page<ProductsRequestEntity> findAllByWriterAndStatus(User user, DataStatus dataStatus,
        Pageable pageable);
}
