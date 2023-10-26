package com.metalancer.backend.products.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.domain.AssetFile;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsWishEntity;
import com.metalancer.backend.products.repository.ProductsAssetFileRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.products.repository.ProductsTagRepository;
import com.metalancer.backend.products.repository.ProductsThumbnailRepository;
import com.metalancer.backend.products.repository.ProductsViewsRepository;
import com.metalancer.backend.products.repository.ProductsWishRepository;
import com.metalancer.backend.users.entity.CartEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.CartRepository;
import com.metalancer.backend.users.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class ProductsDetailServiceImpl implements ProductsDetailService {

    private final ProductsRepository productsRepository;
    private final ProductsWishRepository productsWishRepository;
    private final ProductsTagRepository productsTagRepository;
    private final CartRepository cartRepository;
    private final ProductsThumbnailRepository productsThumbnailRepository;
    private final ProductsViewsRepository productsViewsRepository;
    private final ProductsAssetFileRepository productsAssetFileRepository;
    private final UserRepository userRepository;

    @Override
    public String getProductDetailSharedLink(Long productId) {
        ProductsEntity foundProductsEntity = productsRepository.findProductByIdAndStatus(productId,
            DataStatus.ACTIVE);
        return foundProductsEntity.getSharedLink();
    }

    @Override
    public boolean toggleProductWish(PrincipalDetails user, Long productId) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        ProductsEntity foundProductsEntity = productsRepository.findProductByIdAndStatus(productId,
            DataStatus.ACTIVE);
        Optional<ProductsWishEntity> productsWishEntity = productsWishRepository.findByUserAndProduct(
            foundUser, foundProductsEntity);
        if (productsWishEntity.isEmpty()) {
            ProductsWishEntity createdProductsWish = ProductsWishEntity.builder()
                .productsEntity(foundProductsEntity)
                .user(foundUser).build();
            productsWishRepository.save(createdProductsWish);
            return true;
        }
        ProductsWishEntity foundProductsWish = productsWishEntity.get();
        if (foundProductsWish.getStatus().equals(DataStatus.ACTIVE)) {
            foundProductsWish.deleteProductsWish();
            return false;
        } else {
            foundProductsWish.restoreProductsWish();
            return true;
        }
    }

    @Override
    public ProductsDetail getProductDetail(PrincipalDetails user, Long productId) {
        ProductsEntity foundProductsEntity = productsRepository.findProductByIdAndStatus(productId,
            DataStatus.ACTIVE);
        foundProductsEntity.addViewCnt();
        ProductsDetail response = foundProductsEntity.toProductsDetail();
        if (user != null) {
            User foundUser = user.getUser();
            foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
                () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
            );
            Optional<ProductsWishEntity> foundProductsWishEntity = productsWishRepository.findByUserAndProduct(
                foundUser, foundProductsEntity);
            response.setHasWish(foundProductsWishEntity.isPresent());
            Optional<CartEntity> foundCartEntity = cartRepository.findCartByUserAndAsset(foundUser,
                foundProductsEntity);
            response.setHasCart(foundCartEntity.isPresent() && foundCartEntity.get().getStatus()
                .equals(DataStatus.ACTIVE));
            //        response.setHasOrder();
        }
        List<String> tagList = productsTagRepository.findTagListByProduct(foundProductsEntity);
        response.setTagList(tagList);

        ProductsDetail productsDetail = foundProductsEntity.toProductsDetail();
        getProductsDetailTagList(foundProductsEntity, productsDetail);
        AssetFile assetFile = getProductsDetailAssetFile(foundProductsEntity,
            productsDetail);
        productsDetail.setAssetFile(assetFile);
        response.setAssetFile(assetFile);
        return response;
    }

    @Override
    public ProductsDetail getProductDetailBySharedLink(PrincipalDetails user, String link) {
        ProductsEntity foundProductsEntity = productsRepository.findProductBySharedLinkAndStatus(
            link,
            DataStatus.ACTIVE);
        foundProductsEntity.addViewCnt();
        ProductsDetail response = foundProductsEntity.toProductsDetail();
        if (user != null) {
            User foundUser = user.getUser();
            foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
                () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
            );
            Optional<ProductsWishEntity> foundProductsWishEntity = productsWishRepository.findByUserAndProduct(
                foundUser, foundProductsEntity);
            response.setHasWish(foundProductsWishEntity.isPresent());
            Optional<CartEntity> foundCartEntity = cartRepository.findCartByUserAndAsset(foundUser,
                foundProductsEntity);
            response.setHasCart(foundCartEntity.isPresent() && foundCartEntity.get().getStatus()
                .equals(DataStatus.ACTIVE));
            //        response.setHasOrder();
        }
        List<String> tagList = productsTagRepository.findTagListByProduct(foundProductsEntity);
        response.setTagList(tagList);

        ProductsDetail productsDetail = foundProductsEntity.toProductsDetail();
        getProductsDetailTagList(foundProductsEntity, productsDetail);
        AssetFile assetFile = getProductsDetailAssetFile(foundProductsEntity,
            productsDetail);
        productsDetail.setAssetFile(assetFile);
        response.setAssetFile(assetFile);
        return response;
    }

    private AssetFile getProductsDetailAssetFile(ProductsEntity savedProductsEntity,
        ProductsDetail productsDetail) {
        List<String> thumbnailUrlList = getProductsThumbnailList(savedProductsEntity,
            productsDetail);
        log.info("썸네일 목록 조회");
        List<String> viewUrlList = productsViewsRepository.findAllUrlByProduct(
            savedProductsEntity);
        log.info("3D 뷰 데이터 조회");
        ProductsAssetFileEntity productsAssetFileEntity = productsAssetFileRepository.findByProducts(
            savedProductsEntity);

        AssetFile assetFile = productsAssetFileEntity.toAssetFile();
        assetFile.setThumbnailUrlList(thumbnailUrlList);
        assetFile.setViewUrlList(viewUrlList);
        assetFile.setZipFileUrl("");
        return assetFile;
    }

    @Nullable
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

    private void getProductsDetailTagList(ProductsEntity savedProductsEntity,
        ProductsDetail productsDetail) {
        List<String> tagList = productsTagRepository.findTagListByProduct(savedProductsEntity);
        productsDetail.setTagList(tagList);
    }
}