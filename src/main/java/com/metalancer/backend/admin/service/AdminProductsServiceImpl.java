package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.ProductsList;
import com.metalancer.backend.admin.dto.AdminProductDTO.DeleteList;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.InvalidParamException;
import com.metalancer.backend.products.domain.AssetFile;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsAssetFileRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.products.repository.ProductsTagRepository;
import com.metalancer.backend.products.repository.ProductsThumbnailRepository;
import com.metalancer.backend.products.repository.ProductsViewsRepository;
import com.metalancer.backend.users.entity.CreatorEntity;
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
    private final ProductsTagRepository productsTagRepository;
    private final ProductsViewsRepository productsViewsRepository;
    private final ProductsThumbnailRepository productsThumbnailRepository;

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

    @Override
    public ProductsDetail getProductDetail(Long productId) {
        ProductsEntity foundProductsEntity = productsRepository.findAdminProductById(productId);
        List<String> tagList = productsTagRepository.findTagListByProduct(foundProductsEntity);
        CreatorEntity creatorEntity = foundProductsEntity.getCreatorEntity();
        long taskCnt = productsRepository.countAllByCreatorEntity(creatorEntity);
        double satisficationRate = 0.0;
        ProductsDetail response = foundProductsEntity.toProductsDetail(taskCnt, satisficationRate);
        response.setTagList(tagList);

        getProductsDetailTagList(foundProductsEntity, response);
        AssetFile assetFile = getProductsDetailAssetFileAfterUploaded(foundProductsEntity,
            response);
        response.setAssetFile(assetFile);
        response.setAssetFile(assetFile);
        return response;
    }

    @Override
    public boolean deleteProduct(Long productId) {
        ProductsEntity foundProductsEntity = productsRepository.findAdminProductById(productId);
        foundProductsEntity.deleteProducts();
        return productsRepository.findOptionalByIdAndStatus(productId, DataStatus.DELETED)
            .isPresent();
    }

    @Override
    public Boolean deleteProductList(DeleteList dto) {
        boolean result = true;
        for (Long productsId : dto.getProductsIdList()) {
            ProductsEntity foundProductsEntity = productsRepository.findAdminProductById(
                productsId);
            foundProductsEntity.deleteProducts();
            result = productsRepository.findOptionalByIdAndStatus(productsId, DataStatus.DELETED)
                .isPresent();
        }
        return result;
    }

    private AssetFile getProductsDetailAssetFileAfterUploaded(ProductsEntity savedProductsEntity,
        ProductsDetail productsDetail) {
        List<String> thumbnailUrlList = getProductsThumbnailList(savedProductsEntity,
            productsDetail);
        log.info("썸네일 목록 조회");
        List<String> viewUrlList = productsViewsRepository.findAllUrlByProduct(
            savedProductsEntity);
        log.info("3D 뷰 데이터 조회");
        ProductsAssetFileEntity productsAssetFileEntity = productsAssetFileRepository.findByProducts(
            savedProductsEntity);

        if (!productsAssetFileEntity.getSuccess()) {
            throw new InvalidParamException(ErrorCode.NOT_EXIST_ASSET);
        }

        AssetFile assetFile = productsAssetFileEntity.toAssetFile();
        assetFile.setThumbnailUrlList(thumbnailUrlList);
        assetFile.setViewUrlList(viewUrlList);
//        assetFile.setZipFileUrl(productsAssetFileEntity.getUrl());
        return assetFile;
    }

    private void getProductsDetailTagList(ProductsEntity savedProductsEntity,
        ProductsDetail productsDetail) {
        List<String> tagList = productsTagRepository.findTagListByProduct(savedProductsEntity);
        productsDetail.setTagList(tagList);
    }

    private List<String> getProductsThumbnailList(ProductsEntity savedProductsEntity,
        ProductsDetail productsDetail) {
        List<String> thumbnailUrlList = productsThumbnailRepository.findAllUrlByProduct(
            savedProductsEntity);
        if (thumbnailUrlList != null && thumbnailUrlList.size() > 0) {
            productsDetail.setThumbnail(thumbnailUrlList.get(0));
            productsDetail.setThumbnailList(thumbnailUrlList);
        }
        return thumbnailUrlList;
    }
}