package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.creators.domain.CreatorAssetList;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsAssetFileRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.products.repository.ProductsWishRepository;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class CreatorReadServiceImpl implements CreatorReadService {

    private final CreatorRepository creatorRepository;
    private final ProductsAssetFileRepository productsAssetFileRepository;
    private final ProductsWishRepository productsWishRepository;
    private final ProductsRepository productsRepository;

    @Override
    public String getCreatorInfo(PrincipalDetails user, Long creatorId) {
        return null;
    }

    @Override
    public Page<CreatorAssetList> getCreatorAssetList(PrincipalDetails user, Long creatorId,
        Pageable pageable) {
        User foundUser = user.getUser();
        CreatorEntity creatorEntity = creatorRepository.findByCreatorId(creatorId);
        Page<CreatorAssetList> response = productsAssetFileRepository.findAllMyAssetList(
            creatorEntity, pageable);
        setAssetListHasWish(foundUser, response);

        return response;
    }

    private void setAssetListHasWish(User foundUser, Page<CreatorAssetList> response) {
        for (CreatorAssetList creatorAssetList : response) {
            ProductsEntity productsEntity = productsRepository.findProductById(
                creatorAssetList.getProductsId());
            boolean hasWish = productsWishRepository.findByUserAndProduct(foundUser,
                productsEntity).isPresent();
            creatorAssetList.setHasWish(hasWish);
        }
    }

    @Override
    public String getCreatorPortfolio(Long creatorId) {
        return null;
    }

    @Override
    public Page<CreatorAssetList> getMyRegisteredAssets(PrincipalDetails user, Pageable pageable) {
        User foundUser = user.getUser();
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        return productsAssetFileRepository.findAllMyAssetList(creatorEntity,
            pageable);
    }
}