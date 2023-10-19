package com.metalancer.backend.users.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.interests.domain.Interests;
import com.metalancer.backend.users.domain.Career;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.dto.UserResponseDTO.BasicInfo;
import com.metalancer.backend.users.dto.UserResponseDTO.IntroAndExperience;
import com.metalancer.backend.users.entity.CareerEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.entity.UserInterestsEntity;
import com.metalancer.backend.users.repository.CareerRepository;
import com.metalancer.backend.users.repository.PayedAssetsRepository;
import com.metalancer.backend.users.repository.UserInterestsRepository;
import com.metalancer.backend.users.repository.UserRepository;
import java.util.List;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserInterestsRepository userInterestsRepository;
    private final PayedAssetsRepository payedAssetsRepository;
    private final CareerRepository careerRepository;

    @Override
    public boolean updateToCreator(PrincipalDetails user) {
        try {
            user.getUser().changeToCreator();

            // 이외 데이터 처리

            return true;
        } catch (BaseException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Page<PayedAssets> getPayedAssetList(User user, Pageable pageable) {
        return payedAssetsRepository.findAllPayedAssetList(user, pageable);
    }

    @Override
    public BasicInfo getBasicInfo(PrincipalDetails userPrincipalDetails) {
        User foundUser = userPrincipalDetails.getUser();
        List<UserInterestsEntity> userInterestsEntityList = userInterestsRepository.findAllByUser(
            foundUser);
        List<Interests> interests = userInterestsEntityList.stream()
            .map(UserInterestsEntity::toDomain).toList();

        return BasicInfo.builder().profileImg(foundUser.getProfileImg()).nickname(
                foundUser.getNickname())
            .email(foundUser.getEmail())
            .job(foundUser.getJob()).link(foundUser.getLink())
            .introduction(foundUser.getIntroduction()).interestsList(interests).build();
    }

    @Override
    public IntroAndExperience getIntroAndExperience(PrincipalDetails user) {
        User foundUser = user.getUser();
        List<CareerEntity> careerEntities = careerRepository.findAllByUser(foundUser);
        List<Career> careerList = careerEntities.stream().map(CareerEntity::toDomain).toList();
        return IntroAndExperience.builder().introduction(foundUser.getCareerIntroduction())
            .careerList(careerList).build();
    }
}