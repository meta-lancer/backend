package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.entity.Products;
import com.metalancer.backend.users.entity.Creator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductsRepository {

    Products findProductById(Long productsId);

    Products findProductByIdAndStatus(Long productsId, DataStatus status);

    Page<Products> findProductsListByCreator(Creator creator, Pageable pageable);

    Page<Products> findProductsListByCreatorAndStatus(Creator creator, DataStatus status,
        Pageable pageable);

    Page<Products> findAllByCreator(Creator creator, Pageable pageable);

    Page<Products> findAllByCreatorAndStatus(Creator creator, DataStatus status, Pageable pageable);

    Page<HotPickAsset> findNewProductList(Pageable pageable);

    Page<HotPickAsset> findSaleProductList(Pageable pageable);

    Page<HotPickAsset> findFreeProductList(PeriodType period, Pageable pageable);

    Page<HotPickAsset> findChargeProductList(PeriodType period, Pageable pageable);
}
