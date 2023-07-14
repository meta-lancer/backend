package com.metalancer.backend.member.service;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.member.domain.Email;
import com.metalancer.backend.member.dto.AuthRequestDTO;
import com.metalancer.backend.member.entity.User;
import com.metalancer.backend.member.oauth.KakaoDTO;
import com.metalancer.backend.member.oauth.KakaoOauth;
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

    private final KakaoOauth kakaoOauth;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void kakaoLogin(String code) throws Exception {
        String kakaoAccessToken = kakaoOauth.getKakaoInfo(code);
        KakaoDTO kakaoTokenResponse = kakaoOauth.getUserInfoWithToken(kakaoAccessToken);

//        KakaoDTO userInfo = kakaoOauth.getUserInfoWithToken(
//            kakaoTokenResponse.get);
//        log.info("회원 정보 입니다.{}", userInfo);
//
//        userService.createUser(userInfo.getKakao_account().getEmail());
//        return BaseResponse(memberService.kakaoLogin(dto));
    }

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