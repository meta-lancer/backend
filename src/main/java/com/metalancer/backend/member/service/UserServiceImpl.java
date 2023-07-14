package com.metalancer.backend.member.service;

import com.metalancer.backend.common.constants.ApplicationText;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.utils.RandomStringGenerator;
import com.metalancer.backend.member.dto.MemberRequestDTO;
import com.metalancer.backend.member.entity.ApproveLink;
import com.metalancer.backend.member.entity.User;
import com.metalancer.backend.member.repository.ApproveLinkRepository;
import com.metalancer.backend.member.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ApproveLinkRepository approveLinkRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public Long createUser(MemberRequestDTO.CreateRequest dto) throws MessagingException {
        User createdUser = createEmailUser(dto);
        createdUser = userRepository.save(createdUser);
        User foundUser = userRepository.findById(createdUser.getId()).orElseThrow(
                () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        createdApproveLink(foundUser);
        return foundUser.getId();
    }

    private void createdApproveLink(User foundUser) throws MessagingException {
        String approveLink = new RandomStringGenerator().generateRandomString(12);
        ApproveLink createdApproveLink = ApproveLink.builder().email(foundUser.getEmail().value()).approveLink(approveLink).build();
        createdApproveLink = approveLinkRepository.save(createdApproveLink);
        ApproveLink foundApproveLink = approveLinkRepository.findById(createdApproveLink.getId()).orElseThrow(
                () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        sendApproveEmailToUser(foundUser, foundApproveLink);
    }

    private void sendApproveEmailToUser(User foundUser, ApproveLink foundApproveLink) throws MessagingException {
        String approveLink = foundApproveLink.getApproveLink();
        HashMap<String, String> emailValues = new HashMap<>();
        emailValues.put("url", approveLink);
        emailService.sendMail(foundUser.getEmail().value(), ApplicationText.REGISTER_LINK_EMAIL_TITLE, "approveUser", emailValues);
    }

    private User createEmailUser(MemberRequestDTO.CreateRequest dto) {
        String encryptedPassword = dto.setPasswordEncrypted(passwordEncoder);
        User createdUser = User.builder().email(dto.getEmail()).password(encryptedPassword).loginType(LoginType.NORMAL).build();
        createdUser.setNormalUsername();
        createdUser.setPending();
        return createdUser;
    }

    @Override
    public User approveUserByLink(String link) {
        link = link.replace(ApplicationText.BASE_URL, "");
        ApproveLink foundApproveLink = approveLinkRepository.findByApproveLink(link).orElseThrow(
                () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        foundApproveLink.approve();
        return userRepository.findByEmail(foundApproveLink.getEmail())
                .orElseThrow(() -> new BaseException(ErrorCode.LOGIN_DENIED));
    }
}