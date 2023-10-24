package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsTagEntity;
import com.metalancer.backend.products.entity.QProductsEntity;
import com.metalancer.backend.products.entity.QProductsTagEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRepositoryImpl implements ProductsRepository {

    private final ProductsJpaRepository productsJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final String product = "상품";

    @Override
    public ProductsEntity findProductById(Long productsId) {
        return productsJpaRepository.findById(productsId).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
    }

    @Override
    public ProductsEntity findProductBySharedLinkAndStatus(String sharedLink, DataStatus status) {
        Optional<ProductsEntity> productsEntity = productsJpaRepository.findBySharedLinkContains(
            sharedLink);
        if (productsEntity.isEmpty()) {
            throw new BaseException(ErrorCode.NOT_FOUND);
        }
        ProductsEntity foundProductsEntity = productsEntity.get();
        if (!foundProductsEntity.getStatus().equals(DataStatus.ACTIVE)) {
            throw new BaseException(ErrorCode.PRODUCTS_STATUS_ERROR);
        }
        return foundProductsEntity;
    }

    @Override
    public ProductsEntity findProductByIdAndStatus(Long productsId, DataStatus status) {
        Optional<ProductsEntity> foundProducts = productsJpaRepository.findById(productsId);
        if (foundProducts.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        ProductsEntity product = foundProducts.get();
        product.isProductsStatusEqualsActive();
        return product;
    }

    @Override
    public Page<ProductsEntity> findProductsListByCreator(CreatorEntity creatorEntity,
        Pageable pageable) {
        return productsJpaRepository.findAllByCreatorEntityAndStatus(creatorEntity, pageable,
            DataStatus.ACTIVE);
    }

    @Override
    public Page<ProductsEntity> findProductsListByCreatorAndStatus(CreatorEntity creatorEntity,
        DataStatus status,
        Pageable pageable) {
        return productsJpaRepository.findAllByCreatorEntityAndStatus(creatorEntity, status,
            pageable);
    }

    public Page<ProductsEntity> findAllByCreator(CreatorEntity creatorEntity, Pageable pageable) {
        return productsJpaRepository.findAllByCreatorEntityAndStatus(creatorEntity, pageable,
            DataStatus.ACTIVE);
    }

    @Override
    public Page<ProductsEntity> findAllByCreatorAndStatus(CreatorEntity creatorEntity,
        DataStatus status,
        Pageable pageable) {
        return productsJpaRepository.findAllByCreatorEntityAndStatus(creatorEntity, status,
            pageable);
    }

    @Override
    public Page<HotPickAsset> findNewProductList(Pageable pageable) {
        return productsJpaRepository.findAllByStatusOrderByCreatedAtDesc(DataStatus.ACTIVE,
                pageable)
            .map(ProductsEntity::toHotPickAsset);
    }

    @Override
    public Page<HotPickAsset> findSaleProductList(Pageable pageable) {
        return null;
    }

    @Override
    public Page<HotPickAsset> findFreeProductList(PeriodType period, Pageable pageable) {
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = null;
        if (period.equals(PeriodType.WEEKLY)) {
            startDateTime = endDateTime.minusWeeks(1);
        } else {
            startDateTime = endDateTime.minusMonths(1);
        }
        return productsJpaRepository.findAllByPriceAndStatusAndCreatedAtBetweenOrderByViewCntDesc(0,
                DataStatus.ACTIVE,
                startDateTime, endDateTime, pageable)
            .map(ProductsEntity::toHotPickAsset);
    }

    @Override
    public Page<HotPickAsset> findChargeProductList(PeriodType period, Pageable pageable) {
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = null;
        if (period.equals(PeriodType.WEEKLY)) {
            startDateTime = endDateTime.minusWeeks(1);
        } else {
            startDateTime = endDateTime.minusMonths(1);
        }
        return productsJpaRepository.findAllByPriceIsGreaterThanAndStatusAndCreatedAtBetweenOrderByViewCntDesc(
                0,
                DataStatus.ACTIVE,
                startDateTime,
                endDateTime,
                pageable)
            .map(ProductsEntity::toHotPickAsset);
    }

    @Override
    public void save(ProductsEntity createdProductsEntity) {
        productsJpaRepository.save(createdProductsEntity);
    }

    @Override
    public long countAllByCreatorEntity(CreatorEntity creatorEntity) {
        return productsJpaRepository.countAllByCreatorEntity(creatorEntity);
    }

    @Override
    public Page<ProductsEntity> findAllDistinctByTagListAndStatus(List<String> tagList,
        DataStatus status, Pageable pageable) {
        return productsJpaRepository.findDistinctProductsByTagNamesAndStatus(tagList, status,
            pageable);
    }

    @Override
    public Page<ProductsEntity> findAllByStatusWithPriceOption(DataStatus dataStatus,
        List<Integer> priceOption, Pageable pageable) {
        QProductsEntity qProductsEntity = QProductsEntity.productsEntity;
        BooleanBuilder whereClause = new BooleanBuilder();
        DataStatus activeStatus = DataStatus.ACTIVE;
        // 가격 옵션
        setWhereClauseWithPriceOptions(priceOption, whereClause);
        whereClause.and(qProductsEntity.status.eq(activeStatus));
        List<ProductsEntity> response = queryFactory.selectFrom(
                qProductsEntity)
            .where(whereClause)
            .offset(pageable.getOffset())
            .orderBy(
                qProductsEntity.createdAt.desc()
            )
            .limit(pageable.getPageSize())
            .fetch();
        long total = queryFactory.selectFrom(qProductsEntity).where(whereClause)
            .fetchCount();
        return new PageImpl<>(response, pageable, total);
    }

    @Override
    public Page<ProductsEntity> findAllDistinctByTagListAndStatusWithPriceOption(
        List<String> tagList, DataStatus dataStatus, List<Integer> priceOption, Pageable pageable) {
        QProductsEntity qProductsEntity = QProductsEntity.productsEntity;
        BooleanBuilder whereClause = new BooleanBuilder();
        DataStatus activeStatus = DataStatus.ACTIVE;

        // tagList에 맞게
        setWhereClauseWithTagList(tagList, whereClause);
        // 가격 옵션
        setWhereClauseWithPriceOptions(priceOption, whereClause);

        whereClause.and(qProductsEntity.status.eq(activeStatus));
        List<ProductsEntity> response = queryFactory.selectFrom(
                qProductsEntity)
            .where(whereClause)
            .offset(pageable.getOffset())
            .orderBy(
                qProductsEntity.createdAt.desc()
            )
            .limit(pageable.getPageSize())
            .fetch();
        long total = queryFactory.selectFrom(qProductsEntity).where(whereClause)
            .fetchCount();
        return new PageImpl<>(response, pageable, total);
    }

    private void setWhereClauseWithPriceOptions(List<Integer> priceOptions,
        BooleanBuilder whereClause) {
        for (Integer priceOption : priceOptions) {
            switch (priceOption) {
                case 1 -> {
                    whereClause.or(QProductsEntity.productsEntity.price.between(0, 10000));
                }
                case 2 -> {
                    whereClause.or(QProductsEntity.productsEntity.price.between(10000, 50000));
                }
                case 3 -> {
                    whereClause.or(QProductsEntity.productsEntity.price.between(50000, 100000));
                }
                case 4 -> {
                    whereClause.or(QProductsEntity.productsEntity.price.between(100000, 500000));
                }
                case 5 -> {
                    whereClause.or(QProductsEntity.productsEntity.price.between(500000, 1000000));
                }
                case 6 -> {
                    whereClause.or(QProductsEntity.productsEntity.price.goe(1000000));
                }
            }
        }
    }

    private void setWhereClauseWithTagList(List<String> tagList, BooleanBuilder whereClause) {
        for (String tag : tagList) {
            // tag마다 ProductsTag에서 해당되는 고유한 ProductsEntity만 선별
            whereClause.or(
                QProductsEntity.productsEntity.in(
                    getProductsForTag(tag)));
        }
    }

    private List<ProductsEntity> getProductsForTag(String tag) {
        return queryFactory
            .selectFrom(QProductsTagEntity.productsTagEntity)
            .distinct()
            .where(QProductsTagEntity.productsTagEntity.name.eq(tag))
            .fetch()
            .stream()
            .map(ProductsTagEntity::getProductsEntity)
            .toList();
    }

    @Override
    public Page<ProductsEntity> findAllByStatus(DataStatus status, Pageable pageable) {
        return productsJpaRepository.findAllByStatusOrderByCreatedAtDesc(status, pageable);
    }
}
