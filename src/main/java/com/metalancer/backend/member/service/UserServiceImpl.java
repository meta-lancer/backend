package com.metalancer.backend.member.service;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.member.dto.MemberRequestDTO;
import com.metalancer.backend.member.entity.User;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long createUser(MemberRequestDTO.CreateRequest dto) {
        User createdUser = createEmailUser(dto);
        createdUser = userRepository.save(createdUser);
        User foundUser = userRepository.findById(createdUser.getUserId()).orElseThrow(
                () -> new BaseException(ErrorCode.SIGNUP_FAILED)
        );
        return foundUser.getUserId();
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
        return null;
    }
}