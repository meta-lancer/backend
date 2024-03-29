package com.metalancer.backend.request.repository;

import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import com.metalancer.backend.category.entity.ProductsRequestTypeEntity;
import com.metalancer.backend.category.repository.ProductsRequestTypeJpaRepository;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.request.domain.ProductsRequest;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Create;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Update;
import com.metalancer.backend.request.entity.ProductsRequestAndTypeEntity;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import com.metalancer.backend.users.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRequestRepositoryImpl implements ProductsRequestRepository,
    ProductsRequestRepositoryCustom {

    private final ProductsRequestJpaRepository productsRequestJpaRepository;
    private final ProductsRequestTypeJpaRepository productsRequestTypeJpaRepository;
    private final ProductsRequestAndTypeJpaRepository productsRequestAndTypeJpaRepository;

    @Override
    public Page<ProductsRequest> findAll(List<String> requestTypeOptions,
        Pageable pageable) {
        Page<ProductsRequest> response = null;
        if (requestTypeOptions != null && requestTypeOptions.size() > 0) {
            response = productsRequestJpaRepository.findAllByRequestTypeOptions(
                requestTypeOptions,
                pageable).map(ProductsRequestEntity::toDomain);
        } else {
            response = productsRequestJpaRepository.findAllByStatusOrderByCreatedAtDesc(
                    DataStatus.ACTIVE, pageable)
                .map(ProductsRequestEntity::toDomain);
        }
        for (ProductsRequest productsRequest : response) {
            ProductsRequestEntity productsRequestEntity = productsRequestJpaRepository.findById(
                productsRequest.getProductsRequestId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND)
            );
            List<RequestCategory> requestCategoryList = getRequestCategories(
                productsRequestEntity);
            productsRequest.setProductionRequestTypeList(requestCategoryList);
        }

        return response;
    }

    @Override
    public ProductsRequest findDetail(Long requestId) {
        ProductsRequestEntity productsRequestEntity = productsRequestJpaRepository.findProductsRequestEntityByIdAndStatus(
            requestId, DataStatus.ACTIVE).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
        productsRequestEntity.addViewCnt();
        ProductsRequest response = productsRequestEntity.toDomain();
        List<RequestCategory> requestCategoryList = getRequestCategories(
            productsRequestEntity);
        response.setProductionRequestTypeList(requestCategoryList);

        return response;
    }

    @Override
    public ProductsRequestEntity findEntity(Long requestId) {
        return productsRequestJpaRepository.findProductsRequestEntityByIdAndStatus(
            requestId, DataStatus.ACTIVE).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
    }

    @Override
    public ProductsRequest save(User user, Create dto) {
        ProductsRequestEntity createdProductsRequestEntity = ProductsRequestEntity.builder()
            .writer(user)
            .title(dto.getTitle()).content(
                dto.getContent()).productsRequestStatus(dto.getProductsRequestStatus())
            .fileName(dto.getFileName())
            .fileUrl(dto.getFileUrl())
            .relatedLink(dto.getRelatedLink())
            .build();
        productsRequestJpaRepository.save(createdProductsRequestEntity);
        for (String productsRequestType : dto.getProductionRequestTypeList()) {
            createProductsRequestAndType(createdProductsRequestEntity, productsRequestType);
        }
        ProductsRequest response = productsRequestJpaRepository.findProductsRequestEntityByIdAndStatus(
            createdProductsRequestEntity.getId(), DataStatus.ACTIVE).orElseThrow(
            () -> new NotFoundException(ErrorCode.FAIL_TO_CREATE)
        ).toDomain();
        List<RequestCategory> requestCategoryList = getRequestCategories(
            createdProductsRequestEntity);
        response.setProductionRequestTypeList(requestCategoryList);

        return response;
    }

    @NotNull
    private List<RequestCategory> getRequestCategories(
        ProductsRequestEntity createdProductsRequestEntity) {
        List<ProductsRequestAndTypeEntity> productsRequestAndTypeEntities = productsRequestAndTypeJpaRepository.findAllByProductsRequestEntity(
            createdProductsRequestEntity);
        List<RequestCategory> requestCategoryList = productsRequestAndTypeEntities.stream()
            .map(ProductsRequestAndTypeEntity::toRequestCategory).toList();
        return requestCategoryList;
    }

    private void createProductsRequestAndType(ProductsRequestEntity createdProductsRequestEntity,
        String productsRequestType) {
        ProductsRequestTypeEntity productsRequestTypeEntity = productsRequestTypeJpaRepository.findByName(
                productsRequestType)
            .orElseThrow(() -> new NotFoundException("제작요청 카테고리: ", ErrorCode.NOT_FOUND));
        ProductsRequestAndTypeEntity productsRequestAndTypeEntity = ProductsRequestAndTypeEntity.builder()
            .productsRequestEntity(createdProductsRequestEntity)
            .productsRequestTypeEn(productsRequestTypeEntity.getName())
            .productsRequestTypeKor(productsRequestTypeEntity.getNameKor())
            .build();
        productsRequestAndTypeJpaRepository.save(productsRequestAndTypeEntity);
    }

    @Override
    public ProductsRequest update(User user, Update dto,
        ProductsRequestEntity productsRequestEntity) {
        productsRequestEntity.update(dto.getTitle(),
            dto.getContent(), dto.getProductsRequestStatus(), dto.getRelatedLink());
        if (dto.getFileUrl() != null && !dto.getFileUrl().isBlank() && dto.getFileName() != null
            && !dto.getFileName().isBlank()) {
            productsRequestEntity.updateFile(dto.getFileName(), dto.getFileUrl());
        }
        updateRequestTypeList(dto, productsRequestEntity);
        ProductsRequest response = productsRequestEntity.toDomain();
        List<RequestCategory> requestCategoryList = getRequestCategories(
            productsRequestEntity);
        response.setProductionRequestTypeList(requestCategoryList);

        return response;
    }

    private void updateRequestTypeList(Update dto, ProductsRequestEntity productsRequestEntity) {
        List<ProductsRequestAndTypeEntity> productsRequestAndTypeEntities = productsRequestAndTypeJpaRepository.findAllByProductsRequestEntity(
            productsRequestEntity);
        productsRequestAndTypeJpaRepository.deleteAll(productsRequestAndTypeEntities);
        for (String requestType : dto.getProductionRequestTypeList()) {
            createProductsRequestAndType(productsRequestEntity, requestType);
        }
    }

    @Override
    public Page<ProductsRequest> findAllByUser(User user, Pageable pageable) {
        return productsRequestJpaRepository.findAllByWriterAndStatus(user, DataStatus.ACTIVE,
            pageable).map(ProductsRequestEntity::toDomain);
    }
}
