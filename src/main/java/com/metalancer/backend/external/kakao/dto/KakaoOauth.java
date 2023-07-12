package com.metalancer.backend.external.kakao.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
@Component
public class KakaoOauth {

    @Value("${spring.security.oauth2.client.registration.kakao.clientId}")
    private String KAKAO_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.kakao.clientSecret}")
    private String KAKAO_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.kakao.redirectUri}")
    private String KAKAO_REDIRECT_URL;
    @Value("${spring.security.oauth2.client.provider.kakao.authUri}")
    private String KAKAO_AUTH_URI;
    @Value("${spring.security.oauth2.client.provider.kakao.userInfoUri}")
    private String KAKAO_USER_INFO_URI;

    public String getKakaoInfo(String code) throws Exception {
        if (code == null) {
            throw new Exception("Failed get authorization code");
        }
        log.info("인가 코드를 이용하여 토큰을 받습니다. {}", code);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET);
            params.add("code", code);
            params.add("redirect_uri", KAKAO_REDIRECT_URL);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params,
                headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                KAKAO_AUTH_URI,
                kakaoTokenRequest,
                String.class
            );
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            return (String) jsonObj.get("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("API call failed");
        }

    }

    public KakaoDTO getUserInfoWithToken(String accessToken) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            KAKAO_USER_INFO_URI,
            kakaoProfileRequest,
            String.class
        );

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");

        long id = (long) jsonObj.get("id");
        String email = String.valueOf(account.get("email"));

        //뽑아온 id, email 가 DB에 있는지 확인하고 없으면 등록한다.
        return KakaoDTO.builder()
            .id(id)
            .email(email)
            .build();
    }
}