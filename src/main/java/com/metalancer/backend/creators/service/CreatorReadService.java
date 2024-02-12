package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.creators.domain.CommissionSales;
import com.metalancer.backend.creators.domain.CreatorAssetList;
import com.metalancer.backend.creators.domain.ManageAsset;
import com.metalancer.backend.creators.domain.ManageCommission;
import com.metalancer.backend.creators.domain.PaymentInfoManagement;
import com.metalancer.backend.users.domain.Portfolio;
import com.metalancer.backend.users.dto.UserResponseDTO.IntroAndCareer;
import com.metalancer.backend.users.dto.UserResponseDTO.OtherCreatorBasicInfo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CreatorReadService {

    Page<CreatorAssetList> getCreatorAssetList(PrincipalDetails user, Long creatorId,
        Pageable pageable);

    List<Portfolio> getCreatorPortfolio(Long creatorId);

    Page<CreatorAssetList> getMyRegisteredAssets(PrincipalDetails user, Pageable pageable);

    Page<ManageAsset> getMyManageAssetList(PrincipalDetails user, Pageable pageable);

    List<Portfolio> getMyPortfolio(PrincipalDetails user);

    OtherCreatorBasicInfo getCreatorBasicInfo(PrincipalDetails user, Long creatorId);

    IntroAndCareer getCreatorIntroAndCareer(PrincipalDetails user, Long creatorId);

    PaymentInfoManagement getMyPaymentInfoManagement(PrincipalDetails user);

    Page<ManageCommission> getMyManageCommissionList(PrincipalDetails user, Pageable pageable);

    Page<CommissionSales> getMyManageCommissionSaleList(PrincipalDetails user, Pageable pageable);
}