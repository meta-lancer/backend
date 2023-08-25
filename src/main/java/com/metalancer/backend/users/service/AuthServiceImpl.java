package com.metalancer.backend.users.service;

import static com.metalancer.backend.common.constants.ObjectText.LOGIN_USER;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.users.dto.AuthRequestDTO;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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