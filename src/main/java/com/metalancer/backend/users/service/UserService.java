package com.metalancer.backend.users.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.dto.UserRequestDTO.CreateCareerRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateBasicInfo;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateCareerIntroRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateCareerRequest;
import com.metalancer.backend.users.dto.UserResponseDTO.BasicInfo;
import com.metalancer.backend.users.dto.UserResponseDTO.IntroAndCareer;
import com.metalancer.backend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    boolean updateToCreator(PrincipalDetails user);

    Page<PayedAssets> getPayedAssetList(User user, Pageable pageable);

    BasicInfo getBasicInfo(PrincipalDetails user);

    IntroAndCareer getIntroAndCareer(PrincipalDetails user);

    IntroAndCareer updateCareer(Long careerId, PrincipalDetails user, UpdateCareerRequest dto);

    IntroAndCareer deleteCareer(Long careerId, PrincipalDetails user);

    IntroAndCareer createCareer(PrincipalDetails user, CreateCareerRequest dto);

    IntroAndCareer updateCareerIntro(PrincipalDetails user, UpdateCareerIntroRequest dto);

    BasicInfo updateBasicInfo(PrincipalDetails user, UpdateBasicInfo dto);
}