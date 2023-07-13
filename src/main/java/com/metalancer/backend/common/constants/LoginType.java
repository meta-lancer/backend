package com.metalancer.backend.common.constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum LoginType {
    NORMAL("email"), KAKAO("kakao"), NAVER("naver"), GOOGLE("google");

    private final String provider;

    LoginType(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }

    public static LoginType setLoginType(String provider) {
        if (provider.equals(LoginType.KAKAO.getProvider())) {
            log.info("카카오 로그인 요청");
            return LoginType.KAKAO;
        } else if (provider.equals(LoginType.GOOGLE.getProvider())) {
            log.info("구글 로그인 요청");
            return LoginType.GOOGLE;
        } else if (provider.equals(LoginType.NAVER.getProvider())) {
            log.info("네이버 로그인 요청");
            return LoginType.NAVER;
        } else {
            log.info("알번 로그인 요청");
            return LoginType.NORMAL;
        }
    }
}
