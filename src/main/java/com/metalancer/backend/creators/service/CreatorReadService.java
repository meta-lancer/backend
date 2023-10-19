package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.creators.domain.CreatorAssetList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CreatorReadService {

    String getCreatorInfo(PrincipalDetails user, Long creatorId);

    Page<CreatorAssetList> getCreatorAssetList(PrincipalDetails user, Long creatorId,
        Pageable pageable);

    String getCreatorPortfolio(Long creatorId);

    Page<CreatorAssetList> getMyRegisteredAssets(PrincipalDetails user, Pageable pageable);
}