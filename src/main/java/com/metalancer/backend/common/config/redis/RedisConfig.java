package com.metalancer.backend.common.config.redis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
// maxInactiveIntervalInSeconds는 세션만료시간이고 초단위
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600)
public class RedisConfig extends AbstractHttpSessionApplicationInitializer {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public DefaultCookieSerializer defaultCookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();

//        if (isLocalEnvironment()) {
//            serializer.setUseSecureCookie(false); // Use non-secure cookies for HTTP in local env
//            serializer.setSameSite(null);  // Might help in some local scenarios
//            // ... any other local configurations
//        }
//        else {

        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // Match main domain
//        }
        return serializer;

//        if (isLocalEnvironment()) {
//            // Local environment-specific configurations
//            serializer.setUseSecureCookie(false); // Use non-secure cookies for HTTP in local env
//            serializer.setSameSite(null);  // This might help in some local scenarios
//        } else {
//            // Production or non-local environment-specific configurations
//            serializer.setUseSecureCookie(true); // Ensure cookies are sent over HTTPS only
//            serializer.setSameSite("none");     // Set SameSite to 'none'
//            serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // Match main domain
//        }
//        return serializer;

    }

    private boolean isLocalEnvironment() {
        // Your logic to determine if the environment is local,
        // e.g., based on a specific profile or environment variable.
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            return "localhost".equalsIgnoreCase(hostname) || "127.0.0.1".equals(hostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }
}