package com.metalancer.backend.request.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.request.entity.ProductsRequestCommentsEntity;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRequestCommentsRepositoryImpl implements ProductsRequestCommentsRepository {

    private final ProductsRequestCommentsJpaRepository productsRequestCommentsJpaRepository;

    @Override
    public Page<ProductsRequestCommentsEntity> findAllByPage(
        ProductsRequestEntity productsRequestEntity, Pageable pageable) {
        return productsRequestCommentsJpaRepository.findAllByProductsRequestEntityAndStatus(
            productsRequestEntity, DataStatus.ACTIVE, pageable);
    }

    @Override
    public void save(ProductsRequestCommentsEntity createdProductsRequestCommentsEntity) {
        productsRequestCommentsJpaRepository.save(createdProductsRequestCommentsEntity);
    }

    @Override
    public Optional<ProductsRequestCommentsEntity> findOptionalById(Long commentId) {
        return productsRequestCommentsJpaRepository.findById(commentId);
    }

    @Override
    public ProductsRequestCommentsEntity findByProductsRequestEntityAndId(
        ProductsRequestEntity productsRequestEntity, Long commentId) {
        return productsRequestCommentsJpaRepository.findByProductsRequestEntityAndId(
            productsRequestEntity, commentId).orElseThrow(
            () -> new NotFoundException("productsRequestComment: ", ErrorCode.NOT_FOUND)
        );
    }
}
