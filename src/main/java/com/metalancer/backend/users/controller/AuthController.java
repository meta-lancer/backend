package com.metalancer.backend.users.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.users.dto.AuthRequestDTO;
import com.metalancer.backend.users.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    /*    private final String KEY = "name";*/

    //    @GetMapping
//    public String test(HttpSession session, @RequestParam String name) throws Exception {
//        session.setAttribute(KEY, name);
//        return "redis session saved";
//    }
//
    @GetMapping("/test")
    public Object testValue(HttpSession session, @RequestParam String key,
        @AuthenticationPrincipal PrincipalDetails user) throws Exception {
        log.info("로그인되어있는 유저: {}", session.getAttribute(key));
        log.info("로그인되어있는 유저: {}", user);
        return session.getAttribute(key);
    }

    @PostMapping("/login")
    public BaseResponse<Boolean> emailLogin(HttpSession session,
        @RequestBody AuthRequestDTO.LoginRequest dto) {
        return new BaseResponse<Boolean>(authService.emailLogin(session, dto));
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> emailLogout(HttpSession session) {
        return new BaseResponse<Boolean>(authService.emailLogout(session));
    }
}
