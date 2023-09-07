package com.metalancer.backend.users.service;

import static com.metalancer.backend.common.constants.ObjectText.LOGIN_USER;

import com.metalancer.backend.common.constants.ApplicationText;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.utils.RandomStringGenerator;
import com.metalancer.backend.users.dto.AuthRequestDTO;
import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.dto.UserRequestDTO;
import com.metalancer.backend.users.dto.UserRequestDTO.CreateRequest;
import com.metalancer.backend.users.entity.ApproveLink;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${url.base}")
    private String urlBase;

    private final UserRepository userRepository;
    private final ApproveLinkRepository approveLinkRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserInterestsRepository userInterestsRepository;
    private final UserAgreementRepository userAgreementRepository;

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

    private void createdApproveLink(User foundUser) throws MessagingException {
        String approveLink = getApproveLink();
        ApproveLink createdApproveLink = ApproveLink.builder().email(foundUser.getEmail())
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
        String encryptedPassword = dto.setPasswordEncrypted(passwordEncoder);
        User createdUser = User.builder().email(dto.getEmail()).password(encryptedPassword)
            .loginType(LoginType.NORMAL).job(dto.getJob()).build();
        createdUser.setNormalUsername();
        createdUser.setPending();
        return createdUser;
    }

    @Override
    public AuthResponseDTO.userInfo approveUserByLink(String link) {
        link = link.replace(ApplicationText.BASE_URL, "");
        ApproveLink foundApproveLink = approveLinkRepository.findByApproveLink(link).orElseThrow(
            () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        foundApproveLink.approve();
        User foundUser = userRepository.findByEmail(foundApproveLink.getEmail())
            .orElseThrow(() -> new BaseException(ErrorCode.LOGIN_DENIED));
        foundUser.setActive();
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
}