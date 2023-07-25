package com.metalancer.backend.common.config.security;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.user.entity.User;
import com.metalancer.backend.user.oauth.GoogleUserInfo;
import com.metalancer.backend.user.oauth.KakaoUserInfo;
import com.metalancer.backend.user.oauth.NaverUserInfo;
import com.metalancer.backend.user.oauth.OAuth2UserInfo;
import com.metalancer.backend.user.repository.UserRepository;
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
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String oauthId = providerId;
//        String nickname = oAuth2UserInfo.getName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(loginType.toString()).append("_").append(oauthId);
        String username = stringBuilder.toString();

        Optional<User> optionalUser = userRepository.findByLoginTypeAndOauthIdAndStatus(loginType,
                oauthId,
                DataStatus.ACTIVE);
        User user = null;

        if (optionalUser.isEmpty()) {
            user = User.builder()
                    .oauthId(oauthId)
                    .email(email)
                    .loginType(loginType)
                    .username(username)
                    .build();
            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        log.info("user: {}", user.toString());
        return user;
    }
}