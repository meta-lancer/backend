package com.metalancer.backend.member.domain;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    private String issuer;
    private Map<String, String> client; // key: clientId , value: clientSecret
}