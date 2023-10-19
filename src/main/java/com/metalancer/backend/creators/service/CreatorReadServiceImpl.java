package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.creators.domain.CreatorAssetList;
import com.metalancer.backend.creators.domain.ManageAsset;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsAssetFileRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.products.repository.ProductsTagRepository;
import com.metalancer.backend.products.repository.ProductsWishRepository;
import com.metalancer.backend.users.domain.Career;
import com.metalancer.backend.users.domain.Portfolio;
import com.metalancer.backend.users.dto.UserResponseDTO.IntroAndCareer;
import com.metalancer.backend.users.dto.UserResponseDTO.OtherCreatorBasicInfo;
import com.metalancer.backend.users.entity.CareerEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.CareerRepository;
import com.metalancer.backend.users.repository.PortfolioRepository;
import com.metalancer.backend.users.repository.UserInterestsRepository;
import java.util.ArrayList;
import java.util.List;
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
public class CreatorReadServiceImpl implements CreatorReadService {

    private final CreatorRepository creatorRepository;
    private final ProductsAssetFileRepository productsAssetFileRepository;
    private final ProductsWishRepository productsWishRepository;
    private final ProductsRepository productsRepository;
    private final ProductsTagRepository productsTagRepository;
    private final PortfolioRepository portfolioRepository;
    private final CareerRepository careerRepository;
    private final UserInterestsRepository userInterestsRepository;

    @Override
    public OtherCreatorBasicInfo getCreatorBasicInfo(PrincipalDetails userPrincipalDetails,
        Long creatorId) {
        User foundUser = userPrincipalDetails.getUser();
        CreatorEntity creatorEntity = creatorRepository.findByCreatorId(creatorId);
        User creatorUser = creatorEntity.getUser();
        return creatorUser.toOtherCreatorBasicInfo();
    }

    @Override
    public IntroAndCareer getCreatorIntroAndCareer(PrincipalDetails user, Long creatorId) {
        User foundUser = user.getUser();
        CreatorEntity creatorEntity = creatorRepository.findByCreatorId(creatorId);
        User creatorUser = creatorEntity.getUser();
        return getIntroAndExperience(creatorUser);
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
    public List<Portfolio> getCreatorPortfolio(Long creatorId) {
        CreatorEntity creatorEntity = creatorRepository.findByCreatorId(creatorId);
        return portfolioRepository.findAllByCreator(creatorEntity);
    }

    @Override
    public Page<CreatorAssetList> getMyRegisteredAssets(PrincipalDetails user, Pageable pageable) {
        User foundUser = user.getUser();
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        return productsAssetFileRepository.findAllMyAssetList(creatorEntity,
            pageable);
    }

    @Override
    public Page<ManageAsset> getMyManageAssetList(PrincipalDetails user, Pageable pageable) {
        User foundUser = user.getUser();
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        Page<ProductsEntity> productsEntities = productsRepository.findProductsListByCreator(
            creatorEntity, pageable);
        List<ManageAsset> manageAssets = new ArrayList<>();
        for (ProductsEntity productsEntity : productsEntities) {
            ManageAsset manageAsset = productsEntity.toManageAsset();
            int wishCnt = productsWishRepository.countAllByUserAndProduct(foundUser,
                productsEntity);
            List<String> tagList = productsTagRepository.findTagListByProduct(productsEntity);
            manageAsset.setWishCnt(wishCnt);
            manageAsset.setTagList(tagList);
            manageAssets.add(manageAsset);
        }
        long total = productsRepository.countAllByCreatorEntity(creatorEntity);
        return new PageImpl<>(manageAssets, pageable, total);
    }

    @Override
    public List<Portfolio> getMyPortfolio(PrincipalDetails user) {
        User foundUser = user.getUser();
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        return portfolioRepository.findAllByCreator(creatorEntity);
    }

    private IntroAndCareer getIntroAndExperience(User foundUser) {
        List<CareerEntity> careerEntities = careerRepository.findAllByUser(foundUser);
        List<Career> careerList = careerEntities.stream().map(CareerEntity::toDomain).toList();
        return IntroAndCareer.builder().introduction(foundUser.getCareerIntroduction())
            .careerList(careerList).build();
    }
}