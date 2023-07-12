package com.metalancer.backend.member.service;

import com.metalancer.backend.external.kakao.dto.KakaoDTO;
import com.metalancer.backend.external.kakao.dto.KakaoOauth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KakaoOauth kakaoOauth;

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
}