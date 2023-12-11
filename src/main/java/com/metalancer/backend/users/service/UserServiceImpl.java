package com.metalancer.backend.users.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.ApplyCreator;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.external.aws.s3.S3Service;
import com.metalancer.backend.interests.domain.Interests;
import com.metalancer.backend.orders.repository.OrderPaymentRepository;
import com.metalancer.backend.users.domain.Career;
import com.metalancer.backend.users.domain.OrderStatusList;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.domain.PayedOrder;
import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.dto.UserRequestDTO.CreateCareerRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.CreateInquiryRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateBasicInfo;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateCareerIntroRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.UpdateCareerRequest;
import com.metalancer.backend.users.dto.UserResponseDTO.BasicInfo;
import com.metalancer.backend.users.dto.UserResponseDTO.IntroAndCareer;
import com.metalancer.backend.users.entity.ApproveLink;
import com.metalancer.backend.users.entity.CareerEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.InquiryEntity;
import com.metalancer.backend.users.entity.PortfolioEntity;
import com.metalancer.backend.users.entity.PortfolioReferenceEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.entity.UserInterestsEntity;
import com.metalancer.backend.users.repository.ApproveLinkRepository;
import com.metalancer.backend.users.repository.CareerRepository;
import com.metalancer.backend.users.repository.InquiryRepository;
import com.metalancer.backend.users.repository.PayedAssetsRepository;
import com.metalancer.backend.users.repository.PortfolioReferenceRepository;
import com.metalancer.backend.users.repository.PortfolioRepository;
import com.metalancer.backend.users.repository.UserInterestsRepository;
import com.metalancer.backend.users.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final OrderPaymentRepository orderPaymentRepository;
    private final ApproveLinkRepository approveLinkRepository;
    private final PortfolioRepository portfolioRepository;
    private final S3Service uploadService;
    private final PortfolioReferenceRepository portfolioReferenceRepository;
    private final InquiryRepository inquiryRepository;

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
    public BasicInfo getBasicInfo(PrincipalDetails userPrincipalDetails) {
        User foundUser = userPrincipalDetails.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        List<UserInterestsEntity> userInterestsEntityList = userInterestsRepository.findAllByUser(
            foundUser);
        List<Interests> interests = userInterestsEntityList.stream()
            .map(UserInterestsEntity::toDomain).toList();
        return foundUser.toBasicInfo(interests);
    }

    @Override
    public BasicInfo updateBasicInfo(PrincipalDetails user, UpdateBasicInfo dto) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        if (nicknameUpdateValidate(dto, foundUser)) {
            throw new BaseException(ErrorCode.NICKNAME_UPDATE_COUNT_PROHIBIT);
        }
        userInterestsRepository.deleteAllByUser(foundUser);
        saveUpdatedInterests(dto, foundUser);
        List<Interests> interests = userInterestsRepository.findAllDomainByUser(foundUser);
        foundUser.updateBasicInfo(dto.getProfileImg(), dto.getNickname(), dto.getIntroduction(),
            dto.getLink(), dto.getJob());
        foundUser = userRepository.save(foundUser);
        return foundUser.toBasicInfo(interests);
    }

    @Override
    public Page<PayedOrder> getPaymentList(PrincipalDetails user, String status, String beginDate,
        String endDate, Pageable pageable) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        LocalDateTime beginAt = convertDateToLocalDateTime(beginDate);
        LocalDateTime endAt = convertDateToLocalDateTime(endDate);
        OrderStatus orderStatus = !status.isEmpty() ? OrderStatus.valueOf(status) : null;

        if (orderStatus != null) {
            return orderPaymentRepository.findAllByUserWithOrderStatusAndDateOption(
                foundUser, pageable, beginAt, endAt, orderStatus);
        }
        return orderPaymentRepository.findAllByUserWithDateOption(
            foundUser, pageable, beginAt, endAt);
    }

    @Override
    public List<OrderStatusList> getOrderStatusList() {
        OrderStatus[] orderStatusArr = OrderStatus.values();
        List<OrderStatus> orderStatuses = List.of(orderStatusArr);
        List<OrderStatusList> response = new ArrayList<>();
        for (OrderStatus orderStatus : orderStatuses) {
            OrderStatusList orderStatusList = new OrderStatusList(orderStatus);
            response.add(orderStatusList);
        }
        response.remove(0);
        return response;
    }

    @Override
    public Page<PayedAssets> getPayedAssetList(String status, String beginDate, String endDate,
        PrincipalDetails user, Pageable pageable) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        LocalDateTime beginAt = convertDateToLocalDateTime(beginDate);
        LocalDateTime endAt = convertDateToLocalDateTime(endDate);
        OrderStatus orderStatus = !status.isEmpty() ? OrderStatus.valueOf(status) : null;

        if (orderStatus != null) {
            return payedAssetsRepository.findAllPayedAssetListWithStatusAndDateOption(foundUser,
                pageable, beginAt, endAt, orderStatus);
        }
        return payedAssetsRepository.findAllPayedAssetListWithStatusAndDateOption(foundUser,
            pageable, beginAt, endAt);
    }

    @Override
    public AuthResponseDTO.userInfo getUserInfo(PrincipalDetails user) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        AuthResponseDTO.userInfo response = new AuthResponseDTO.userInfo(foundUser);

        if (DataStatus.PENDING.equals(foundUser.getStatus())) {
            // 이미 가입링크 보낸게 있으면 분기점
            Optional<ApproveLink> approveLink = approveLinkRepository.findByUserAndStatus(foundUser,
                DataStatus.ACTIVE);
            response.setHasApproveLink(approveLink.isPresent());
        }

        Optional<CreatorEntity> creator = creatorRepository.findOptionalByUserAndStatus(foundUser,
            DataStatus.ACTIVE);
        creator.ifPresent(creatorEntity -> response.setCreatorId(creatorEntity.getId()));
        return response;
    }

    @Override
    public boolean applyCreator(MultipartFile[] files, ApplyCreator dto, PrincipalDetails user) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        // 크리에이터 생성 + 승인대기
        CreatorEntity createdCreatorEntity = CreatorEntity.builder().user(foundUser)
            .email(foundUser.getEmail()).build();
        createdCreatorEntity.setPending();
        creatorRepository.save(createdCreatorEntity);

        // 포트폴리오 생성
        CreatorEntity creatorEntity = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.PENDING);
        PortfolioEntity portfolioEntity = PortfolioEntity.builder().creatorEntity(creatorEntity)
            .title(dto.getTitle()).beginAt(dto.getBeginAt()).endAt(dto.getEndAt())
            .workerCnt(dto.getWorkerCnt()).tool(dto.getTool())
            .build();
        portfolioRepository.save(portfolioEntity);

        // 포트폴리오와 관련하여 파일 등록
        if (files != null && files.length > 0) {
            int index = 1;
            List<PortfolioReferenceEntity> portfolioReferenceEntities = new ArrayList<>();
            // 전부 삭제
            for (MultipartFile file : files) {
                // Check if the file is not null and the size is greater than 0
                if (file != null && file.getSize() > 0) {
                    try {
                        String randomFileName = uploadService.getRandomStringForImageName(8);
                        // 업로드한 url
                        String uploadedUrl = uploadService.uploadToPortfolioReference(
                            createdCreatorEntity.getId(), portfolioEntity.getId(), file,
                            randomFileName);
                        PortfolioReferenceEntity createdPortfolioReferenceEntity = PortfolioReferenceEntity.builder()
                            .portfolioEntity(portfolioEntity).url(uploadedUrl).ord(index++)
                            .build();
                        portfolioReferenceEntities.add(createdPortfolioReferenceEntity);
                    } catch (Exception e) {
                        log.error(e.getLocalizedMessage() + ": ", e);
                        throw new BaseException(ErrorCode.REFERENCE_UPLOAD_FAILED);
                    }
                }
            }
            if (portfolioReferenceEntities.size() > 0) {
                portfolioReferenceRepository.saveAll(portfolioReferenceEntities);
            }
        }

        return true;
    }

    @Override
    public boolean createInquiry(PrincipalDetails user, CreateInquiryRequest dto) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        InquiryEntity inquiryEntity = InquiryEntity.builder().build();
        inquiryRepository.save(inquiryEntity);
        Optional<InquiryEntity> foundInquiryEntity = inquiryRepository.findById(
            inquiryEntity.getId());
        return foundInquiryEntity.isPresent();
    }

    private static LocalDateTime convertDateToLocalDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.atStartOfDay();
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
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        return getIntroAndExperience(foundUser);
    }


    @Override
    public IntroAndCareer updateCareer(Long careerId, PrincipalDetails user,
        UpdateCareerRequest dto) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CareerEntity careerEntity = careerRepository.findByIdAndUser(careerId, foundUser);
        careerEntity.update(dto.getTitle(), dto.getDescription(), dto.getBeginAt(), dto.getEndAt());
        return getIntroAndExperience(foundUser);
    }

    @Override
    public IntroAndCareer deleteCareer(Long careerId, PrincipalDetails user) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        careerRepository.deleteCareer(careerId, foundUser);
        return getIntroAndExperience(foundUser);
    }

    @Override
    public IntroAndCareer createCareer(PrincipalDetails user, CreateCareerRequest dto) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        CareerEntity createdCareerEntity = CareerEntity.builder().user(foundUser).title(
                dto.getTitle()).description(dto.getDescription())
            .beginAt(dto.getBeginAt()).endAt(dto.getEndAt()).build();
        careerRepository.save(createdCareerEntity);
        return getIntroAndExperience(foundUser);
    }

    @Override
    public IntroAndCareer updateCareerIntro(PrincipalDetails user, UpdateCareerIntroRequest dto) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        foundUser.setCareerIntroduction(dto.getIntro());
        userRepository.save(foundUser);
        return getIntroAndExperience(foundUser);
    }

    private IntroAndCareer getIntroAndExperience(User foundUser) {
        List<CareerEntity> careerEntities = careerRepository.findAllByUser(foundUser);
        List<Career> careerList = careerEntities.stream().map(CareerEntity::toDomain).toList();
        return IntroAndCareer.builder().introduction(foundUser.getCareerIntroduction())
            .careerList(careerList).build();
    }

}