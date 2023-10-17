package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;

public interface CreatorReadService {

    String getCreatorInfo(PrincipalDetails user, Long creatorId);

    String getCreatorAssetList(PrincipalDetails user, Long creatorId);

    String getCreatorPortfolio(Long creatorId);
}