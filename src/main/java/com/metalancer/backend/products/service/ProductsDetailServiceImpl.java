package com.metalancer.backend.products.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.domain.AssetFile;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsWishEntity;
import com.metalancer.backend.products.repository.*;
import com.metalancer.backend.users.entity.CartEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.CartRepository;
import com.metalancer.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        List<String> thumbnailUrlList = productsThumbnailRepository.findAllUrlByProduct(
                foundProductsEntity);
        List<String> viewUrlList = productsViewsRepository.findAllUrlByProduct(
                foundProductsEntity);
        String zipFileUrl = productsAssetFileRepository.findUrlByProduct(foundProductsEntity);
        AssetFile assetFile = AssetFile.builder().thumbnailUrlList(thumbnailUrlList)
                .viewUrlList(viewUrlList).zipFileUrl(zipFileUrl)
                .build();
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

        List<String> thumbnailUrlList = productsThumbnailRepository.findAllUrlByProduct(
                foundProductsEntity);
        List<String> viewUrlList = productsViewsRepository.findAllUrlByProduct(
                foundProductsEntity);
        String zipFileUrl = productsAssetFileRepository.findUrlByProduct(foundProductsEntity);
        AssetFile assetFile = AssetFile.builder().thumbnailUrlList(thumbnailUrlList)
                .viewUrlList(viewUrlList).zipFileUrl(zipFileUrl)
                .build();
        response.setAssetFile(assetFile);
        return response;
    }
}