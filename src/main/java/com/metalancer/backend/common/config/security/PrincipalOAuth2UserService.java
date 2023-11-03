package com.metalancer.backend.common.config.security;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.oauth.GoogleUserInfo;
import com.metalancer.backend.users.oauth.KakaoUserInfo;
import com.metalancer.backend.users.oauth.NaverUserInfo;
import com.metalancer.backend.users.oauth.OAuth2UserInfo;
import com.metalancer.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    //    private final BCryptPasswordEncoder encoder;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId();
        LoginType loginType = LoginType.setLoginType(provider);

        if (provider.equals(LoginType.KAKAO.getProvider())) {
            oAuth2UserInfo = new KakaoUserInfo((Map) oAuth2User.getAttributes());
        } else if (provider.equals(LoginType.GOOGLE.getProvider())) {
            oAuth2UserInfo = new GoogleUserInfo((Map) oAuth2User.getAttributes());
        } else if (provider.equals(LoginType.NAVER.getProvider())) {
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }

        User user = saveOrGetUser(oAuth2UserInfo, loginType);
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

    private User saveOrGetUser(OAuth2UserInfo oAuth2UserInfo, LoginType loginType) {
        String oauthId = oAuth2UserInfo.getProviderId();
        Optional<User> optionalUser = userRepository.findByLoginTypeAndOauthId(loginType,
                oauthId);
        User user = null;

        if (optionalUser.isEmpty()) {
            checkIfEmailDuplicatedSignUp(oAuth2UserInfo);

            String email = oAuth2UserInfo.getEmail() != null ? oAuth2UserInfo.getEmail() : oauthId + "@naver.com";
            String username = loginType.toString() + "_" + oauthId;
            //        String nickname = oAuth2UserInfo.getName();

            user = User.builder()
                    .oauthId(oauthId)
                    .email(email)
                    .loginType(loginType)
                    .username(username)
                    .build();
            user.setPending();
            user = userRepository.save(user);
            userRepository.findById(user.getId()).orElseThrow(
                    () -> new BaseException(ErrorCode.SIGNUP_FAILED)
            );
        } else {
            user = optionalUser.get();
        }

        log.info("user: {}", user.toString());
        return user;
    }

    private void checkIfEmailDuplicatedSignUp(OAuth2UserInfo oAuth2UserInfo) {
        Optional<User> foundUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        if (foundUser.isPresent()) {
            switch (foundUser.get().getLoginType()) {
                case NORMAL -> throw new BaseException(ErrorCode.EMAIL_SIGNUP_DUPLICATED);
                case KAKAO -> throw new BaseException(ErrorCode.KAKAO_SIGNUP_DUPLICATED);
                case NAVER -> throw new BaseException(ErrorCode.NAVER_SIGNUP_DUPLICATED);
                case GOOGLE -> throw new BaseException(ErrorCode.GOOGLE_SIGNUP_DUPLICATED);
            }
        }
    }
}