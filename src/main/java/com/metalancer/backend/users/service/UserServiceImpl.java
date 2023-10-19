package com.metalancer.backend.users.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.interests.domain.Interests;
import com.metalancer.backend.users.domain.Career;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.dto.UserRequestDTO.CreateCareerRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateBasicInfo;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateCareerIntroRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateCareerRequest;
import com.metalancer.backend.users.dto.UserResponseDTO.BasicInfo;
import com.metalancer.backend.users.dto.UserResponseDTO.IntroAndCareer;
import com.metalancer.backend.users.entity.CareerEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.entity.UserInterestsEntity;
import com.metalancer.backend.users.repository.CareerRepository;
import com.metalancer.backend.users.repository.PayedAssetsRepository;
import com.metalancer.backend.users.repository.UserInterestsRepository;
import com.metalancer.backend.users.repository.UserRepository;
import java.util.ArrayList;
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
    private final CreatorRepository creatorRepository;

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
        return foundUser.toBasicInfo(interests);
    }

    @Override
    public BasicInfo updateBasicInfo(PrincipalDetails user, UpdateBasicInfo dto) {
        User foundUser = user.getUser();
        if (nicknameUpdateValidate(dto, foundUser)) {
            throw new BaseException(ErrorCode.NICKNAME_UPDATE_COUNT_PROHIBIT);
        }
        userInterestsRepository.deleteAllByUser(foundUser);
        saveUpdatedInterests(dto, foundUser);
        List<Interests> interests = userInterestsRepository.findAllDomainByUser(foundUser);
        foundUser.updateBasicInfo(dto.getProfileImg(), dto.getNickname(), dto.getIntroduction(),
            dto.getLink(), dto.getJob());
        return foundUser.toBasicInfo(interests);
    }

    private static boolean nicknameUpdateValidate(UpdateBasicInfo dto, User foundUser) {
        return !dto.getNickname().equals(foundUser.getNickname())
            && foundUser.checkNicknameUpdatedBefore();
    }

    private void saveUpdatedInterests(UpdateBasicInfo dto, User foundUser) {
        List<UserInterestsEntity> newUserInterestsEntityList = new ArrayList<>();
        for (String interest : dto.getInterestsList()) {
            UserInterestsEntity userInterestsEntity = UserInterestsEntity.builder().user(foundUser)
                .interestsName(interest).build();
            newUserInterestsEntityList.add(userInterestsEntity);
        }
        userInterestsRepository.saveAll(newUserInterestsEntityList);
    }

    @Override
    public IntroAndCareer getIntroAndCareer(PrincipalDetails user) {
        User foundUser = user.getUser();
        return getIntroAndExperience(foundUser);
    }


    @Override
    public IntroAndCareer updateCareer(Long careerId, PrincipalDetails user,
        UpdateCareerRequest dto) {
        User foundUser = user.getUser();
        CareerEntity careerEntity = careerRepository.findByIdAndUser(careerId, foundUser);
        careerEntity.update(dto.getTitle(), dto.getDescription(), dto.getBeginAt(), dto.getEndAt());
        return getIntroAndExperience(foundUser);
    }

    @Override
    public IntroAndCareer deleteCareer(Long careerId, PrincipalDetails user) {
        User foundUser = user.getUser();
        careerRepository.deleteCareer(careerId, foundUser);
        return getIntroAndExperience(foundUser);
    }

    @Override
    public IntroAndCareer createCareer(PrincipalDetails user, CreateCareerRequest dto) {
        User foundUser = user.getUser();
        CareerEntity createdCareerEntity = CareerEntity.builder().user(foundUser).title(
                dto.getTitle()).description(dto.getDescription())
            .beginAt(dto.getBeginAt()).endAt(dto.getEndAt()).build();
        careerRepository.save(createdCareerEntity);
        return getIntroAndExperience(foundUser);
    }

    @Override
    public IntroAndCareer updateCareerIntro(PrincipalDetails user, UpdateCareerIntroRequest dto) {
        User foundUser = user.getUser();
        foundUser.setCareerIntroduction(dto.getIntro());
        return getIntroAndExperience(foundUser);
    }

    private IntroAndCareer getIntroAndExperience(User foundUser) {
        List<CareerEntity> careerEntities = careerRepository.findAllByUser(foundUser);
        List<Career> careerList = careerEntities.stream().map(CareerEntity::toDomain).toList();
        return IntroAndCareer.builder().introduction(foundUser.getCareerIntroduction())
            .careerList(careerList).build();
    }
}