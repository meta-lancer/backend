package com.metalancer.backend.products.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.ProductsType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.InvalidParamException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.domain.AssetFile;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.products.domain.RequestOption;
import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsWishEntity;
import com.metalancer.backend.products.repository.ProductsAssetFileRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.products.repository.ProductsRequestOptionRepository;
import com.metalancer.backend.products.repository.ProductsTagRepository;
import com.metalancer.backend.products.repository.ProductsThumbnailRepository;
import com.metalancer.backend.products.repository.ProductsViewsRepository;
import com.metalancer.backend.products.repository.ProductsWishRepository;
import com.metalancer.backend.users.entity.CartEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.PayedAssetsEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.CartRepository;
import com.metalancer.backend.users.repository.PayedAssetsRepository;
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
    private final ProductsRequestOptionRepository productsRequestOptionRepository;
    private final PayedAssetsRepository payedAssetsRepository;


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

        CreatorEntity creatorEntity = foundProductsEntity.getCreatorEntity();
        long taskCnt = productsRepository.countAllByCreatorEntity(creatorEntity);
        double satisficationRate = 0.0;
        ProductsDetail response = foundProductsEntity.toProductsDetail(taskCnt, satisficationRate);
        if (user != null) {
            User foundUser = user.getUser();
            foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
                () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
            );
            // 로그인 유저의 조회수만..! 본인인 경우 조회수 증가 x
            if (!foundProductsEntity.getCreatorEntity().getUser().getId()
                .equals(foundUser.getId())) {
                foundProductsEntity.addViewCnt();
            }
            Optional<ProductsWishEntity> foundProductsWishEntity = productsWishRepository.findByUserAndProduct(
                foundUser, foundProductsEntity);
            response.setHasWish(foundProductsWishEntity.isPresent());
            if (foundProductsEntity.getProductsType().equals(ProductsType.REQUEST)) {
                List<CartEntity> foundCartEntityList = cartRepository.findAllCartByUserAndAsset(
                    foundUser, foundProductsEntity);
                response.setHasCart(foundCartEntityList.size() > 0 && foundCartEntityList.stream()
                    .anyMatch(cart -> DataStatus.ACTIVE.equals(cart.getStatus())));
            } else {
                Optional<CartEntity> foundCartEntity = cartRepository.findCartByUserAndAsset(
                    foundUser,
                    foundProductsEntity);
                response.setHasCart(foundCartEntity.isPresent() && foundCartEntity.get().getStatus()
                    .equals(DataStatus.ACTIVE));
                List<PayedAssetsEntity> freePayedAssetEntityList = payedAssetsRepository.findAllByUserAndProductsAndStatus(
                    foundUser, foundProductsEntity, DataStatus.ACTIVE);
                response.setHasOrder(freePayedAssetEntityList.size() > 0);
            }

        }
        List<String> tagList = productsTagRepository.findTagListByProduct(foundProductsEntity);
        response.setTagList(tagList);
        getProductsDetailTagList(foundProductsEntity, response);

        // request 인 경우
        if (foundProductsEntity.getProductsType().equals(ProductsType.REQUEST)) {
            List<RequestOption> productsRequestOptionEntityList = productsRequestOptionRepository.findAllByProducts(
                foundProductsEntity);
            response.setRequestOptionList(productsRequestOptionEntityList);
        }
        AssetFile assetFile = getProductsDetailAssetFileAfterUploaded(foundProductsEntity,
            response);
        response.setAssetFile(assetFile);
        return response;
    }

    @Override
    public ProductsDetail getProductDetailBySharedLink(PrincipalDetails user, String link) {
        ProductsEntity foundProductsEntity = productsRepository.findProductBySharedLinkAndStatus(
            link, DataStatus.ACTIVE);

        CreatorEntity creatorEntity = foundProductsEntity.getCreatorEntity();
        long taskCnt = productsRepository.countAllByCreatorEntity(creatorEntity);
        double satisficationRate = 0.0;
        ProductsDetail response = foundProductsEntity.toProductsDetail(taskCnt, satisficationRate);
        if (user != null) {
            User foundUser = user.getUser();
            foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
                () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
            );
            // 로그인 유저의 조회수만..! 본인인 경우 조회수 증가 x
            if (!foundProductsEntity.getCreatorEntity().getUser().getId()
                .equals(foundUser.getId())) {
                foundProductsEntity.addViewCnt();
            }
            Optional<ProductsWishEntity> foundProductsWishEntity = productsWishRepository.findByUserAndProduct(
                foundUser, foundProductsEntity);
            response.setHasWish(foundProductsWishEntity.isPresent());
            if (foundProductsEntity.getProductsType().equals(ProductsType.REQUEST)) {
                List<CartEntity> foundCartEntityList = cartRepository.findAllCartByUserAndAsset(
                    foundUser, foundProductsEntity);
                response.setHasCart(foundCartEntityList.size() > 0 && foundCartEntityList.stream()
                    .anyMatch(cart -> DataStatus.ACTIVE.equals(cart.getStatus())));
            } else {
                Optional<CartEntity> foundCartEntity = cartRepository.findCartByUserAndAsset(
                    foundUser,
                    foundProductsEntity);
                response.setHasCart(foundCartEntity.isPresent() && foundCartEntity.get().getStatus()
                    .equals(DataStatus.ACTIVE));
                List<PayedAssetsEntity> freePayedAssetEntityList = payedAssetsRepository.findAllByUserAndProductsAndStatus(
                    foundUser, foundProductsEntity, DataStatus.ACTIVE);
                response.setHasOrder(freePayedAssetEntityList.size() > 0);
            }
        }
        List<String> tagList = productsTagRepository.findTagListByProduct(foundProductsEntity);
        response.setTagList(tagList);
        getProductsDetailTagList(foundProductsEntity, response);

        // request 인 경우
        if (foundProductsEntity.getProductsType().equals(ProductsType.REQUEST)) {
            List<RequestOption> productsRequestOptionEntityList = productsRequestOptionRepository.findAllByProducts(
                foundProductsEntity);
            response.setRequestOptionList(productsRequestOptionEntityList);
        }

        AssetFile assetFile = getProductsDetailAssetFile(foundProductsEntity,
            response);
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

        return assetFile;
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

        if (savedProductsEntity.getProductsType().equals(ProductsType.NORMAL)
            && !productsAssetFileEntity.getSuccess()) {
            throw new InvalidParamException(ErrorCode.NOT_EXIST_ASSET);
        }

        AssetFile assetFile = productsAssetFileEntity.toAssetFile();
        assetFile.setThumbnailUrlList(thumbnailUrlList);
        assetFile.setViewUrlList(viewUrlList);

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