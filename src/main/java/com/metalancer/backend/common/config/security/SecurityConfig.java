package com.metalancer.backend.common.config.security;

import com.metalancer.backend.member.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final PrincipalOAuth2UserService principalOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.oauth2Login()
//
//            .successHandler(successHandler())
//            .userInfoEndpoint()
//            .userService(principalOAuth2UserService);
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/user/**", "/h2-console").authenticated()
                .requestMatchers("/api/auth/test").hasRole("USER")
                .requestMatchers("/api/sell/**").hasRole("SELLER")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()

//            .and()
//            .formLogin()
//            .loginPage("/loginForm") //미인증자일경우 해당 uri를 호출
//            .loginProcessingUrl(
//                "/login") //login 주소가 호출되면 시큐리티가 낚아 채서(post로 오는것) 대신 로그인 진행 -> 컨트롤러를 안만들어도 된다.
//            .defaultSuccessUrl("/")

                .and()
                .oauth2Login()
//            .loginPage("/loginForm")'
//            .defaultSuccessUrl("/")
                .successHandler(successHandler())
                .userInfoEndpoint()
                .userService(
                        principalOAuth2UserService);//구글 로그인이 완료된(구글회원) 뒤의 후처리가 필요함 . Tip.코드x, (엑세스 토큰+사용자 프로필 정보를 받아옴)

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
//            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
//
//            String id = defaultOAuth2User.getAttributes().get("id").toString();
//            String body = """
//                {"id":"%s"}
//                """.formatted(id);
//
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//
//            PrintWriter writer = response.getWriter();
//            writer.println(body);
//            writer.flush();
        });
    }
}