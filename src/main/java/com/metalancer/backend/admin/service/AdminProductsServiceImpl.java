package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.ProductsList;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsAssetFileRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AdminProductsServiceImpl implements AdminProductsService {

    private final ProductsRepository productsRepository;
    private final ProductsAssetFileRepository productsAssetFileRepository;

    @Override
    public Page<ProductsList> getAdminProductsList(Pageable pageable) {
        Page<ProductsEntity> productsEntityList = productsRepository.findAllAdminProductsList(
            pageable);
        List<ProductsList> productsLists = new ArrayList<>();
        for (ProductsEntity productsEntity : productsEntityList) {
            ProductsList productsList = productsEntity.toAdminProductsList();
            Optional<ProductsAssetFileEntity> productsAssetFileEntity = productsAssetFileRepository.findOptionalEntityByProducts(
                productsEntity);
            if (productsAssetFileEntity.isEmpty()) {
                continue;
            }
            productsList.setAssetFileSuccess(productsAssetFileEntity.get().getSuccess());
            productsLists.add(productsList);
        }
        Long totalCnt = productsRepository.countAllProducts();
        return new PageImpl<>(productsLists, pageable, totalCnt);
    }
}