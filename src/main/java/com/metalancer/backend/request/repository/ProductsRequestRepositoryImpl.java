package com.metalancer.backend.request.repository;

import com.metalancer.backend.category.entity.ProductsRequestTypeEntity;
import com.metalancer.backend.category.repository.ProductsRequestTypeJpaRepository;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.request.domain.ProductsRequest;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Create;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Update;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import com.metalancer.backend.request.entity.QProductsRequestEntity;
import com.metalancer.backend.users.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

    @Override
    public Page<ProductsRequest> findAll(List<String> requestTypeOptions,
        Pageable pageable) {
        QProductsRequestEntity qProductsRequestEntity = QProductsRequestEntity.productsRequestEntity;
        BooleanBuilder whereClause = new BooleanBuilder();
        DataStatus activeStatus = DataStatus.ACTIVE;
        setWhereClauseWithRequestTypeOptions(requestTypeOptions, qProductsRequestEntity,
            whereClause);
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
        return productsRequestEntity.toDomain();
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
            .writer(user).productionRequestType(dto.getProductionRequestType())
            .title(dto.getTitle()).content(
                dto.getContent()).productsRequestStatus(dto.getProductsRequestStatus()).build();
        productsRequestJpaRepository.save(createdProductsRequestEntity);
        return productsRequestJpaRepository.findProductsRequestEntityByIdAndStatus(
            createdProductsRequestEntity.getId(), DataStatus.ACTIVE).orElseThrow(
            () -> new NotFoundException(ErrorCode.FAIL_TO_CREATE)
        ).toDomain();
    }

    @Override
    public ProductsRequest update(User user, Update dto,
        ProductsRequestEntity productsRequestEntity) {
        productsRequestEntity.update(dto.getProductionRequestType(), dto.getTitle(),
            dto.getContent(), dto.getProductsRequestStatus());
        return productsRequestEntity.toDomain();
    }

    private void setWhereClauseWithRequestTypeOptions(List<String> requestTypeOptions,
        QProductsRequestEntity qProductsRequestEntity, BooleanBuilder whereClause) {
        for (String requestTypeOption : requestTypeOptions) {
            Optional<ProductsRequestTypeEntity> typeEntity = productsRequestTypeJpaRepository.findByName(
                requestTypeOption);
            typeEntity.ifPresent(productsRequestTypeEntity -> whereClause.or(
                qProductsRequestEntity.productionRequestType.eq(productsRequestTypeEntity)));
        }
    }
}
