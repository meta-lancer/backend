package com.metalancer.backend.member.service;

import com.metalancer.backend.common.constants.ApplicationText;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.utils.RandomStringGenerator;
import com.metalancer.backend.member.dto.MemberRequestDTO;
import com.metalancer.backend.member.entity.RegisterLink;
import com.metalancer.backend.member.entity.User;
import com.metalancer.backend.member.repository.RegisterLinkRepository;
import com.metalancer.backend.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RegisterLinkRepository registerLinkRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public Long createUser(MemberRequestDTO.CreateRequest dto) {
        User createdUser = createEmailUser(dto);
        createdUser = userRepository.save(createdUser);
        User foundUser = userRepository.findById(createdUser.getId()).orElseThrow(
                () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        createdRegisterLink(foundUser);
        return foundUser.getId();
    }

    private void createdRegisterLink(User foundUser) {
        String registerLink = new RandomStringGenerator().generateRandomString(12);
        RegisterLink createdRegisterLink = RegisterLink.builder().email(foundUser.getEmail().value()).registerLink(registerLink).build();
        createdRegisterLink = registerLinkRepository.save(createdRegisterLink);
        RegisterLink foundRegisterLink = registerLinkRepository.findById(createdRegisterLink.getId()).orElseThrow(
                () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        String approveLink = foundRegisterLink.getRegisterLink();
        emailService.sendEmail(foundUser.getEmail().value(), ApplicationText.REGISTER_LINK_EMAIL_TITLE, "");
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
        RegisterLink foundRegisterLink = registerLinkRepository.findByRegisterLink(link).orElseThrow(
                () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        foundRegisterLink.approve();
        return userRepository.findByEmail(foundRegisterLink.getEmail())
                .orElseThrow(() -> new BaseException(ErrorCode.LOGIN_DENIED));
    }
}