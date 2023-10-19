package com.metalancer.backend.users.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.dto.UserResponseDTO.BasicInfo;
import com.metalancer.backend.users.dto.UserResponseDTO.IntroAndExperience;
import com.metalancer.backend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    boolean updateToCreator(PrincipalDetails user);

    Page<PayedAssets> getPayedAssetList(User user, Pageable pageable);

    BasicInfo getBasicInfo(PrincipalDetails user);

    IntroAndExperience getIntroAndExperience(PrincipalDetails user);
}