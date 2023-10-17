package com.metalancer.backend.creators.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class CreatorReadServiceImpl implements CreatorReadService {

    @Override
    public String getCreatorInfo(PrincipalDetails user, Long creatorId) {
        return null;
    }

    @Override
    public String getCreatorAssetList(PrincipalDetails user, Long creatorId) {
        return null;
    }

    @Override
    public String getCreatorPortfolio(Long creatorId) {
        return null;
    }
}