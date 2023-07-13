package com.metalancer.backend.member.oauth;

public interface OAuth2UserInfo {

    String getProviderId();

    String getProvider();

    String getEmail();

//    String getName();
}
