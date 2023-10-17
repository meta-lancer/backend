package com.metalancer.backend.common.config.security;

import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
            .requestMatchers("/api/auth/**", "/api/user/**", "/h2-console", "/loginForm",
                "/api/auth/login")
            .authenticated()
            .requestMatchers("/api/auth/test").hasAnyRole("USER", "SELLER", "ADMIN")
            .requestMatchers("/api/creators/**").hasAnyRole("SELLER", "ADMIN")
//            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().permitAll()

            .and()
            //일반 적인 로그인
            .formLogin()
            .loginPage("http://www.metaovis.com") //로그인 페이지 url //미인증자일경우 해당 uri를 호
            .loginProcessingUrl("/api/auth/login") //이 url을 로그인 기능을 담당하게 함
            .defaultSuccessUrl("http://www.metaovis.com") // 성공하면 이 url로 가게 해라
            //                .loginProcessingUrl(
//                        "/login") //login 주소가 호출되면 시큐리티가 낚아 채서(post로 오는것) 대신 로그인 진행 -> 컨트롤러를 안만들어도 된다.
//                .defaultSuccessUrl("/")
            .successHandler(formLoginSuccessHandler())
            .and()
            //OAuth 로그인
            .oauth2Login()
            .loginPage("http://www.metaovis.com") //로그
            .successHandler(successHandler())
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
                response.sendRedirect("http://www.metaovis.com");
            }) // 로그아웃 성공 핸들러
            .deleteCookies("remember-me"); // 로그아웃 후 삭제할 쿠키 지정
        
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler formLoginSuccessHandler() {
        return ((request, response, authentication) -> {
            PrincipalDetails defaultUser = (PrincipalDetails) authentication.getPrincipal();
            User foundUser = defaultUser.getUser();
            foundUser.isUserStatusEqualsActive();
        });
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