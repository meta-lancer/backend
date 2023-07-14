package com.metalancer.backend.member.service;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.member.domain.Email;
import com.metalancer.backend.member.dto.AuthRequestDTO;
import com.metalancer.backend.member.entity.User;
import com.metalancer.backend.member.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.metalancer.backend.common.constants.ObjectText.LOGIN_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean emailLogin(HttpSession session, AuthRequestDTO.LoginRequest dto) {
        User user = userRepository.findByEmail(new Email(dto.getEmail()))
                .orElseThrow(() -> new BaseException(ErrorCode.LOGIN_DENIED));

        user.isUserActive();
        user.isPasswordMatch(passwordEncoder, dto.getPassword());

        session.setAttribute(LOGIN_USER, user);
        return true;
    }
}