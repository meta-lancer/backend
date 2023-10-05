package com.metalancer.backend.request.repository;

import com.metalancer.backend.category.entity.ProductsRequestTypeEntity;
import com.metalancer.backend.category.repository.ProductsRequestTypeJpaRepository;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.request.domain.ProductsRequest;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import com.metalancer.backend.request.entity.QProductsRequestEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        setWhereClauseWithRequestTypeOptions(requestTypeOptions, qProductsRequestEntity, whereClause);
        whereClause.and(qProductsRequestEntity.status.eq(activeStatus));
        List<ProductsRequestEntity> productsRequests = queryFactory.selectFrom(qProductsRequestEntity)
                .where(whereClause)
                .offset(pageable.getOffset())
                .orderBy(
                        qProductsRequestEntity.createdAt.desc()
                )
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.selectFrom(qProductsRequestEntity).where(whereClause).fetchCount();
        List<ProductsRequest> response = productsRequests.stream().map(ProductsRequestEntity::toDomain).toList();
        return new PageImpl<>(response, pageable, total);
    }

    private void setWhereClauseWithRequestTypeOptions(List<String> requestTypeOptions, QProductsRequestEntity qProductsRequestEntity, BooleanBuilder whereClause) {
        for (String requestTypeOption : requestTypeOptions) {
            Optional<ProductsRequestTypeEntity> typeEntity = productsRequestTypeJpaRepository.findByName(requestTypeOption);
            typeEntity.ifPresent(productsRequestTypeEntity -> whereClause.or(qProductsRequestEntity.productionRequestType.eq(productsRequestTypeEntity)));
        }
    }
}
