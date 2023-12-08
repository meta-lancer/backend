package com.metalancer.backend.users.service;

import static com.metalancer.backend.common.constants.ObjectText.LOGIN_USER;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.ApplicationText;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.InvalidParamException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.common.utils.RandomStringGenerator;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.users.dto.AuthRequestDTO;
import com.metalancer.backend.users.dto.AuthRequestDTO.PasswordRequest;
import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.dto.UserRequestDTO;
import com.metalancer.backend.users.dto.UserRequestDTO.CreateOauthRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.CreateRequest;
import com.metalancer.backend.users.entity.ApproveLink;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.entity.UserAgreementEntity;
import com.metalancer.backend.users.entity.UserInterestsEntity;
import com.metalancer.backend.users.repository.ApproveLinkRepository;
import com.metalancer.backend.users.repository.UserAgreementRepository;
import com.metalancer.backend.users.repository.UserInterestsRepository;
import com.metalancer.backend.users.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AuthServiceImpl implements AuthService {

    @Value("${url.base}")
    private String urlBase;

    private final UserRepository userRepository;
    private final ApproveLinkRepository approveLinkRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserInterestsRepository userInterestsRepository;
    private final UserAgreementRepository userAgreementRepository;
    private final CreatorRepository creatorRepository;

    @Override
    public Long createUser(UserRequestDTO.CreateRequest dto) throws MessagingException {
        User createdUser = createEmailUser(dto);
        createdUser = userRepository.save(createdUser);
        User foundUser = userRepository.findById(createdUser.getId()).orElseThrow(
            () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        setUserInterests(foundUser, dto);
        setAgreement(foundUser, dto);
        createdApproveLink(foundUser);
        // ## 포트원 심사를 위해 크리에이터 전환
        foundUser.changeToCreator();
        createCreator(foundUser);
        return foundUser.getId();
    }

    private void setAgreement(User foundUser, CreateRequest dto) {
        UserAgreementEntity savedUserAgreementEntity = UserAgreementEntity.builder().user(foundUser)
            .ageAgree(dto.isAgeAgree())
            .serviceAgree(dto.isServiceAgree()).infoAgree(dto.isInfoAgree())
            .marketingAgree(dto.isMarketingAgree()).statusAgree(
                dto.isStatusAgree()).build();
        userAgreementRepository.save(savedUserAgreementEntity);
    }

    private void setUserInterests(User foundUser, CreateRequest dto) {
        List<UserInterestsEntity> userInterestsEntities = new ArrayList<>();
        for (String interests : dto.getInterests()) {
            UserInterestsEntity savedUserInterestsEntity = UserInterestsEntity.builder()
                .user(foundUser)
                .interestsName(interests).build();
            userInterestsEntities.add(savedUserInterestsEntity);
        }
        if (userInterestsEntities.size() > 0) {
            userInterestsRepository.saveAll(userInterestsEntities);
        }

    }

    private void setAgreement(User foundUser, CreateOauthRequest dto) {
        UserAgreementEntity savedUserAgreementEntity = UserAgreementEntity.builder().user(foundUser)
            .ageAgree(dto.isAgeAgree())
            .serviceAgree(dto.isServiceAgree()).infoAgree(dto.isInfoAgree())
            .marketingAgree(dto.isMarketingAgree()).statusAgree(
                dto.isStatusAgree()).build();
        userAgreementRepository.save(savedUserAgreementEntity);
    }

    private void setUserInterests(User foundUser, CreateOauthRequest dto) {
        List<UserInterestsEntity> userInterestsEntities = new ArrayList<>();
        for (String interests : dto.getInterests()) {
            UserInterestsEntity savedUserInterestsEntity = UserInterestsEntity.builder()
                .user(foundUser)
                .interestsName(interests).build();
            userInterestsEntities.add(savedUserInterestsEntity);
        }
        if (userInterestsEntities.size() > 0) {
            userInterestsRepository.saveAll(userInterestsEntities);
        }

    }

    private void createdApproveLink(User foundUser) throws MessagingException {
        String approveLink = getApproveLink();
        ApproveLink createdApproveLink = ApproveLink.builder().user(foundUser)
            .email(foundUser.getEmail())
            .approveLink(approveLink).build();
        createdApproveLink = approveLinkRepository.save(createdApproveLink);
        ApproveLink foundApproveLink = approveLinkRepository.findById(createdApproveLink.getId())
            .orElseThrow(
                () -> new BaseException(ErrorCode.SIGNUP_FAILED)
            );
        sendApproveEmailToUser(foundUser, foundApproveLink);
    }

    private String getApproveLink() {
        return urlBase + new RandomStringGenerator().generateRandomString(12);
    }

    private void sendApproveEmailToUser(User foundUser, ApproveLink foundApproveLink)
        throws MessagingException {
        String approveLink = foundApproveLink.getApproveLink();
        HashMap<String, String> emailValues = new HashMap<>();
        emailValues.put("url", approveLink);
        emailService.sendMail(foundUser.getEmail(), ApplicationText.REGISTER_LINK_EMAIL_TITLE,
            "approve", emailValues);
    }

    private User createEmailUser(UserRequestDTO.CreateRequest dto) {
        Optional<User> foundUser = userRepository.findByEmail(dto.getEmail());
        if (foundUser.isPresent()) {
            throw new BaseException(ErrorCode.EMAIL_SIGNUP_DUPLICATED);
        }
        String encryptedPassword = dto.setPasswordEncrypted(passwordEncoder);
        User createdUser = User.builder().email(dto.getEmail()).password(encryptedPassword)
            .loginType(LoginType.NORMAL).job(dto.getJob()).build();
        createdUser.setNormalUsername();
        createdUser.setPending();
        return createdUser;
    }

    @Override
    public AuthResponseDTO.userInfo approveUserByLink(String link) {
        link = urlBase + link;
        log.info("승인 링크 - {}", link);
        ApproveLink foundApproveLink = approveLinkRepository.findByApproveLink(link).orElseThrow(
            () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        foundApproveLink.approve();
        User foundUser = userRepository.findByEmail(foundApproveLink.getEmail())
            .orElseThrow(() -> new BaseException(ErrorCode.LOGIN_DENIED));
        foundUser.setActive();
        String randomNickName = "";
        foundUser.setFirstNickName(randomNickName);

        Optional<CreatorEntity> foundCreator = creatorRepository.findOptionalByUserAndStatus(
            foundUser, DataStatus.PENDING);
        foundCreator.ifPresent(CreatorEntity::setActive);

        return new AuthResponseDTO.userInfo(foundUser);
    }

    @Override
    public boolean emailLogin(HttpSession session, AuthRequestDTO.LoginRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new BaseException(ErrorCode.LOGIN_NOT_FOUND_ID_PW));
        user.isUserStatusEqualsActive();
        user.isPasswordMatch(passwordEncoder, dto.getPassword());
        session.setAttribute(LOGIN_USER, user);
        return true;
    }

    @Override
    public boolean emailLogout(HttpSession session) {
        session.removeAttribute(LOGIN_USER);
        return true;
    }

    @Override
    public Long createCreator(CreateRequest dto) throws MessagingException {
        User createdUser = createEmailUser(dto);
        createdUser = userRepository.save(createdUser);
        User foundUser = userRepository.findById(createdUser.getId()).orElseThrow(
            () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        setUserInterests(foundUser, dto);
        setAgreement(foundUser, dto);
        createdApproveLink(foundUser);
        createCreator(foundUser);
        CreatorEntity foundCreator = creatorRepository.findByUserAndStatus(foundUser,
            DataStatus.PENDING);
        return foundCreator.getId();
    }

    @Override
    public Long createOauthUser(PrincipalDetails user, CreateOauthRequest dto)
        throws MessagingException {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );

        Optional<User> emailFoundUser =
            foundUser.getLoginType().equals(LoginType.NAVER) ? Optional.empty()
                : userRepository.findByEmail(dto.getEmail());
        foundUser.setEmailIfNotDuplicated(dto.getEmail(), emailFoundUser);
        setUserInterests(foundUser, dto);
        setAgreement(foundUser, dto);
        foundUser.setActive();
        String randomNickName = "";
        foundUser.setFirstNickName(randomNickName);
//        createdApproveLink(foundUser);

        // # 포트원 심사를 위해 무조건 허용
//        if (!dto.isNormalUser()) {
        // 심사 필요없음
        createCreatorWithOAuthUser(foundUser);
//        }
        return foundUser.getId();
    }

    private void createCreator(User foundUser) {
        CreatorEntity createdCreator = CreatorEntity.builder().user(foundUser)
            .email(foundUser.getEmail()).build();
        creatorRepository.save(createdCreator);
        createdCreator.setPending();
    }

    private void createCreatorWithOAuthUser(User foundUser) {
  
        CreatorEntity createdCreator = CreatorEntity.builder().user(foundUser)
            .email(foundUser.getEmail()).build();
        creatorRepository.save(createdCreator);

    }

    public void hasPrincipalDetails(PrincipalDetails user) {
        if (user == null) {
            throw new BaseException(ErrorCode.LOGIN_REQUIRED);
        }
    }

    @Override
    public boolean resetPassword(String email) {

        Optional<User> foundUser = userRepository.findByEmail(email);
        if (foundUser.isPresent()) {
            String tempPassword =
                (char) ((int) (Math.random() * 26) + 97) + RandomStringUtils.randomAlphanumeric(10)
                    + ((int) (Math.random() * 99) + 1);
            ;
            String encodeTempPassword = passwordEncoder.encode(tempPassword);
            foundUser.get().setPassword(encodeTempPassword);

            emailService.sendEmail(foundUser.get().getEmail(), "메타오비스 임시비밀번호 발급 메일입니다.",
                "임시비밀번호: " + tempPassword + " 입니다. 로그인 후, 비밀번호를 변경해주세요");

            return true;

        }
        return false;
    }

    @Override
    public Boolean resetMyPassword(PrincipalDetails user, PasswordRequest dto) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        foundUser.isOriginalPasswordMatch(passwordEncoder, dto.getOriginalPassword());
        if (!dto.newPasswordEquals()) {
            throw new InvalidParamException(ErrorCode.NEW_PASSWORD_NOT_MATCHED);
        }
        foundUser.changeNewPassword(passwordEncoder, dto.getNewPassword1());
        return passwordEncoder.matches(dto.getNewPassword1(), foundUser.getPassword());
    }
}