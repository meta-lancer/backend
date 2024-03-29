package com.metalancer.backend.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.metalancer.backend.users.repository.LoginLogsRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final PrincipalOAuth2UserService principalOAuth2UserService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final MemberLoginFailService memberLoginFailService;
    private final LoginLogsRepository loginLogsRepository;
    private final PrincipalDetailsService principalDetailsService;

    @Value("${spring.security.oauth2.client.targetUrl}")
    private String targetUrl;

    @Value("${spring.loginPage}")
    private String loginPage;

    @Value("${spring.successUrl}")
    private String successUrl;

    @Value("${spring.failUrl}")
    private String failUrl;

    int VALID_SECONDS = 60 * 60 * 24 * 7; // 1주일

    @Value("${spring.security.rememberMe.key}")
    private String rememberMeKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }


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
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/swagger-ui/index.html").denyAll()
            .requestMatchers("/h2-console",
                "/loginForm", "/api/products/{productId}/wish")
            .authenticated()
            .requestMatchers("/api/products/**").permitAll()
            .requestMatchers("/api/cart/**", "/api/orders/**").hasAnyRole("USER", "SELLER", "ADMIN")
            .requestMatchers("/api/creator/**").hasAnyRole("SELLER", "ADMIN")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().permitAll()

            .and()
            //일반 적인 로그인
            .formLogin()
            .loginPage(loginPage) //로그인 페이지 url //미인증자일경우 해당 uri를 호
            .loginProcessingUrl("/api/auth/login") //이 url을 로그인 기능을 담당하게 함
            .defaultSuccessUrl(successUrl) // 성공하면 이 url로 가게 해라
            //                .loginProcessingUrl(
//                        "/login") //login 주소가 호출되면 시큐리티가 낚아 채서(post로 오는것) 대신 로그인 진행 -> 컨트롤러를 안만들어도 된다.
//                .defaultSuccessUrl("/")
            .successHandler(formLoginSuccessHandler())
            .failureHandler(memberLoginFailService)
            .and()
            //OAuth 로그인
            .oauth2Login()
            .loginPage(loginPage) //로그
            .defaultSuccessUrl(successUrl)
//                .authorizationEndpoint()
//                .authorizationRequestRepository(customAuthorizationRequestRepository())
            .successHandler(oauth2SuccessHandler())
            .failureHandler(authenticationFailureHandler())
            .failureUrl("https://www.metaovis.com?error=duplicated_register")
            .userInfoEndpoint()
            .userService(
                principalOAuth2UserService);//구글 로그인이 완료된(구글회원) 뒤의 후처리가 필요함 . Tip.코드x, (엑세스 토큰+사용자 프로필 정보를 받아옴)

        http.logout()
            .logoutUrl("/api/auth/logout")   // 로그아웃 처리 URL (= form action url)
//            .logoutSuccessUrl("/login") // 로그아웃 성공 후 targetUrl,
            // logoutSuccessHandler 가 있다면 효과 없으므로 주석처리.
            .addLogoutHandler((request, response, authentication) -> {
                // 사실 굳이 내가 세션 무효화하지 않아도 됨.
                // LogoutFilter가 내부적으로 해줌.
                HttpSession session = request.getSession();
                if (session != null) {
                    session.invalidate();
                }
            })  // 로그아웃 핸들러 추가
            .logoutSuccessHandler((request, response, authentication) -> {
                response.sendRedirect(loginPage);
            }) // 로그아웃 성공 핸들러
            .deleteCookies("SESSION", "remember-me"); // 로그아웃 후 삭제할 쿠키 지정

        http.exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler);

        http.rememberMe()
            .key(rememberMeKey)
            .tokenValiditySeconds(VALID_SECONDS)
            .rememberMeParameter("rememberMe");

        return http.build();
    }

//    @Bean
//    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> customAuthorizationRequestRepository() {
//        return new HttpSessionOAuth2AuthorizationRequestRepository() {
//            @Override
//            public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
//                super.saveAuthorizationRequest(authorizationRequest, request, response);
//                for (Cookie cookie : response.getCookies()) {
//                    if ("oauth2_auth_request".equals(cookie.getName())) {
//                        // Set samesite and secure attributes for the cookie
//                        response.setHeader("Set-Cookie", cookie.toString() + "; SameSite=none; Secure");
//                    }
//                }
//            }
//        };
//    }

    @Bean
    public AuthenticationSuccessHandler formLoginSuccessHandler() {
        return ((request, response, authentication) -> {
            PrincipalDetails defaultUser = (PrincipalDetails) authentication.getPrincipal();
            User foundUser = defaultUser.getUser();
            foundUser.isUserStatusEqualsActive();
            String ipAddress = getIpAddress(request);
            loginLogsRepository.saveLog(foundUser, ipAddress);
        });
    }

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        log.info("Client IP: " + ipAddress);
        return ipAddress;
    }


    @Bean
    public AuthenticationSuccessHandler oauth2SuccessHandler() {

        return ((request, response, authentication) -> {
            PrincipalDetails oAuth2User = (PrincipalDetails) authentication.getPrincipal();
            User user = oAuth2User.getUser();
            // 이를 통해 리다이렉트하도록 응답 코드 보낸다.
            response.sendRedirect(targetUrl);
            String ipAddress = getIpAddress(request);
            loginLogsRepository.saveLog(user, ipAddress);
        });
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return ((request, response, authenticationException) -> {
            log.info(request.getRequestURI());
            log.info(request.getPathInfo());
            log.info(request.getRemoteAddr());
            log.info(request.getHeader("Referer"));
            // Extracting request information
            String userAgent = request.getHeader("User-Agent");
            String requestURL = request.getRequestURL().toString();
            String requestURI = request.getRequestURI();
            String queryString = request.getQueryString();

            // Logging the information
            log.info("User-Agent: {}", userAgent);
            log.info("Request URL: {}", requestURL);
            log.info("Request URI: {}", requestURI);
            log.info("Query String: {}", queryString);

            // Get the error message from the exception
            String errorMessage =
                "이유: " + authenticationException.getCause() + authenticationException.getMessage();

            // Logging the error for debugging purposes
            log.error("OAuth2 Authentication Failure: {}", errorMessage, authenticationException);

        });
    }

    @Component
    @RequiredArgsConstructor
    public static class CustomAccessDeniedHandler implements AccessDeniedHandler {

        private final ObjectMapper objectMapper;

        @Override
        public void handle(HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
            log.error("No Authorities", accessDeniedException);
            log.error("Request Uri : {}", request.getRequestURI());

//            ApiResponse<ErrorResponse> apiResponse = ApiResponse.createAuthoritiesError();
//            String responseBody = objectMapper.writeValueAsString(apiResponse);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write(responseBody);
        }
    }

    @Component
    public static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
        }
    }
}