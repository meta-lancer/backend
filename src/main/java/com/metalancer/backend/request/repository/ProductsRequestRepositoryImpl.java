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
import com.metalancer.backend.request.entity.QProductsRequestAndTypeEntity;
import com.metalancer.backend.request.entity.QProductsRequestEntity;
import com.metalancer.backend.users.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRequestRepositoryImpl implements ProductsRequestRepository,
    ProductsRequestRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final ProductsRequestJpaRepository productsRequestJpaRepository;
    private final ProductsRequestTypeJpaRepository productsRequestTypeJpaRepository;
    private final ProductsRequestAndTypeJpaRepository productsRequestAndTypeJpaRepository;

    @Override
    public Page<ProductsRequest> findAll(List<String> requestTypeOptions,
        Pageable pageable) {
        QProductsRequestEntity qProductsRequestEntity = QProductsRequestEntity.productsRequestEntity;
        BooleanBuilder whereClause = new BooleanBuilder();
        DataStatus activeStatus = DataStatus.ACTIVE;
        setWhereClauseWithRequestTypeOptions(requestTypeOptions, whereClause);
        whereClause.and(qProductsRequestEntity.status.eq(activeStatus));
        List<ProductsRequestEntity> productsRequests = queryFactory.selectFrom(
                qProductsRequestEntity)
            .where(whereClause)
            .offset(pageable.getOffset())
            .orderBy(
                qProductsRequestEntity.createdAt.desc()
            )
            .limit(pageable.getPageSize())
            .fetch();
        long total = queryFactory.selectFrom(qProductsRequestEntity).where(whereClause)
            .fetchCount();
        List<ProductsRequest> response = productsRequests.stream()
            .map(ProductsRequestEntity::toDomain).toList();
        return new PageImpl<>(response, pageable, total);
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
        int commentCnt = 0;
        response.setCommentCnt(commentCnt);
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
                dto.getContent()).productsRequestStatus(dto.getProductsRequestStatus()).build();
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
        response.setCommentCnt(0);

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
            productsRequestType).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
        ProductsRequestAndTypeEntity productsRequestAndTypeEntity = ProductsRequestAndTypeEntity.builder()
            .productsRequestEntity(createdProductsRequestEntity)
            .productsRequestTypeEntity(productsRequestTypeEntity)
            .build();
        productsRequestAndTypeJpaRepository.save(productsRequestAndTypeEntity);
    }

    @Override
    public ProductsRequest update(User user, Update dto,
        ProductsRequestEntity productsRequestEntity) {
        productsRequestEntity.update(dto.getTitle(),
            dto.getContent(), dto.getProductsRequestStatus());
        ProductsRequest response = productsRequestEntity.toDomain();
        List<RequestCategory> requestCategoryList = getRequestCategories(
            productsRequestEntity);
        response.setProductionRequestTypeList(requestCategoryList);
        int commentCnt = 0;
        response.setCommentCnt(commentCnt);

        return response;
    }

    private void setWhereClauseWithRequestTypeOptions(List<String> requestTypeOptions,
        BooleanBuilder whereClause) {
        for (String requestTypeOption : requestTypeOptions) {
            Optional<ProductsRequestTypeEntity> typeEntity = productsRequestTypeJpaRepository.findByName(
                requestTypeOption);
            typeEntity.ifPresent(productsRequestTypeEntity -> whereClause.or(
                QProductsRequestAndTypeEntity.productsRequestAndTypeEntity.productsRequestTypeEntity.eq(
                    productsRequestTypeEntity)));
        }
    }
}
