package com.metalancer.backend.users.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.ApplyCreator;
import com.metalancer.backend.users.domain.OrderStatusList;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.domain.PayedOrder;
import com.metalancer.backend.users.dto.AuthResponseDTO.userInfo;
import com.metalancer.backend.users.dto.UserRequestDTO.CreateCareerRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.CreateInquiryRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateBasicInfo;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateCareerIntroRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateCareerRequest;
import com.metalancer.backend.users.dto.UserResponseDTO.BasicInfo;
import com.metalancer.backend.users.dto.UserResponseDTO.IntroAndCareer;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    boolean updateToCreator(PrincipalDetails user);


    BasicInfo getBasicInfo(PrincipalDetails user);

    IntroAndCareer getIntroAndCareer(PrincipalDetails user);

    IntroAndCareer updateCareer(Long careerId, PrincipalDetails user, UpdateCareerRequest dto);

    IntroAndCareer deleteCareer(Long careerId, PrincipalDetails user);

    IntroAndCareer createCareer(PrincipalDetails user, CreateCareerRequest dto);

    IntroAndCareer updateCareerIntro(PrincipalDetails user, UpdateCareerIntroRequest dto);

    BasicInfo updateBasicInfo(PrincipalDetails user, UpdateBasicInfo dto);

    Page<PayedOrder> getPaymentList(PrincipalDetails user, String status, String beginDate,
        String endDate,
        Pageable adjustedPageable);

    List<OrderStatusList> getOrderStatusList();

    Page<PayedAssets> getPayedAssetList(String status, String beginDate, String endDate,
        PrincipalDetails user,
        Pageable pageable);

    userInfo getUserInfo(PrincipalDetails user);

    boolean applyCreator(MultipartFile[] files, ApplyCreator dto, PrincipalDetails user);

    boolean createInquiry(PrincipalDetails user, CreateInquiryRequest dto);
}