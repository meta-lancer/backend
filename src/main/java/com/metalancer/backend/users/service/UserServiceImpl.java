package com.metalancer.backend.users.service;

import com.metalancer.backend.common.constants.ApplicationText;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.utils.RandomStringGenerator;
import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.dto.UserRequestDTO;
import com.metalancer.backend.users.entity.ApproveLink;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.ApproveLinkRepository;
import com.metalancer.backend.users.repository.UserRepository;
import jakarta.mail.MessagingException;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${url.base}")
    private String urlBase;

    private final UserRepository userRepository;
    private final ApproveLinkRepository approveLinkRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public Long createUser(UserRequestDTO.CreateRequest dto) throws MessagingException {
        User createdUser = createEmailUser(dto);
        createdUser = userRepository.save(createdUser);
        User foundUser = userRepository.findById(createdUser.getId()).orElseThrow(
            () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        createdApproveLink(foundUser);
        return foundUser.getId();
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
            .loginType(LoginType.NORMAL).build();
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
}