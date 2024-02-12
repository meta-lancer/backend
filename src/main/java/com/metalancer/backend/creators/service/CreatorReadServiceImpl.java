package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.creators.domain.CommissionSales;
import com.metalancer.backend.creators.domain.CreatorAssetList;
import com.metalancer.backend.creators.domain.ManageAsset;
import com.metalancer.backend.creators.domain.ManageCommission;
import com.metalancer.backend.creators.domain.PaymentInfoManagement;
import com.metalancer.backend.creators.entity.PaymentInfoManagementEntity;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.creators.repository.PaymentInfoManagementRepository;
import com.metalancer.backend.orders.entity.OrderRequestProductsEntity;
import com.metalancer.backend.orders.repository.OrderRequestProductsRepository;
import com.metalancer.backend.products.domain.RequestOption;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsAssetFileRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.products.repository.ProductsRequestOptionRepository;
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
import com.metalancer.backend.users.repository.UserRepository;
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
public class CreatorReadServiceImpl implements CreatorReadService {

    private final CreatorRepository creatorRepository;
    private final ProductsAssetFileRepository productsAssetFileRepository;
    private final ProductsWishRepository productsWishRepository;
    private final ProductsRepository productsRepository;
    private final ProductsTagRepository productsTagRepository;
    private final PortfolioRepository portfolioRepository;
    private final CareerRepository careerRepository;
    private final UserRepository userRepository;
    private final UserInterestsRepository userInterestsRepository;
    private final PaymentInfoManagementRepository paymentInfoManagementRepository;
    private final ProductsRequestOptionRepository productsRequestOptionRepository;
    private final OrderRequestProductsRepository orderRequestProductsRepository;

    @Override
    public OtherCreatorBasicInfo getCreatorBasicInfo(PrincipalDetails user,
        Long creatorId) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CreatorEntity creatorEntity = creatorRepository.findByCreatorId(creatorId);
        User creatorUser = creatorEntity.getUser();
        return creatorUser.toOtherCreatorBasicInfo();
    }

    @Override
    public IntroAndCareer getCreatorIntroAndCareer(PrincipalDetails user, Long creatorId) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CreatorEntity creatorEntity = creatorRepository.findByCreatorId(creatorId);
        User creatorUser = creatorEntity.getUser();
        return getIntroAndExperience(creatorUser);
    }

    @Override
    public Page<CreatorAssetList> getCreatorAssetList(PrincipalDetails user, Long creatorId,
        Pageable pageable) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
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
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        return productsAssetFileRepository.findAllMyAssetList(creatorEntity,
            pageable);
    }

    @Override
    public Page<ManageAsset> getMyManageAssetList(PrincipalDetails user, Pageable pageable) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
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
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
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

    @Override
    public PaymentInfoManagement getMyPaymentInfoManagement(PrincipalDetails user) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        Optional<PaymentInfoManagementEntity> optionalPaymentInfoManagement = paymentInfoManagementRepository.findByCreatorEntity(
            creatorEntity);
        return optionalPaymentInfoManagement.map(
            PaymentInfoManagementEntity::toPaymentInfoManagement).orElse(null);
    }

    @Override
    public Page<ManageCommission> getMyManageCommissionList(PrincipalDetails user,
        Pageable pageable) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        Page<ProductsEntity> productsEntities = productsRepository.findCommissionListByCreator(
            creatorEntity, pageable);
        List<ManageCommission> manageCommissions = new ArrayList<>();
        for (ProductsEntity productsEntity : productsEntities) {
            ManageCommission manageCommission = productsEntity.toManageCommission();
            int wishCnt = productsWishRepository.countAllByUserAndProduct(foundUser,
                productsEntity);
            List<String> tagList = productsTagRepository.findTagListByProduct(productsEntity);
            manageCommission.setWishCnt(wishCnt);
            manageCommission.setTagList(tagList);
            List<RequestOption> requestOptions = productsRequestOptionRepository.findAllByProducts(
                productsEntity);
            manageCommission.setRequestOptions(requestOptions);
            manageCommissions.add(manageCommission);
        }
        return new PageImpl<>(manageCommissions, pageable, productsEntities.getTotalElements());
    }

    @Override
    public Page<CommissionSales> getMyManageCommissionSaleList(PrincipalDetails user,
        Pageable pageable) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        Page<OrderRequestProductsEntity> orderRequestProductsEntities = orderRequestProductsRepository.findAllOrderRequestProductsByCreator(
            creatorEntity, pageable);
        List<CommissionSales> commissionSaleList = new ArrayList<>();
        for (OrderRequestProductsEntity orderRequestProductsEntity : orderRequestProductsEntities) {
            CommissionSales commissionSales = orderRequestProductsEntity.toCommissionSales();
            commissionSaleList.add(commissionSales);
        }
        return new PageImpl<>(commissionSaleList, pageable,
            orderRequestProductsEntities.getTotalElements());
    }
}