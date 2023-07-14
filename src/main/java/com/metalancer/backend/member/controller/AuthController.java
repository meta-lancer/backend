package com.metalancer.backend.member.controller;


import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.member.dto.AuthRequestDTO;
import com.metalancer.backend.member.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final String KEY = "name";

    @GetMapping("/kakao")
    public BaseResponse<Boolean> kakaoLogin(@RequestParam("code") String code) throws Exception {
        authService.kakaoLogin(code);
        return new BaseResponse<Boolean>(true);
    }

    @GetMapping
    public String test(HttpSession session, @RequestParam String name) throws Exception {
        session.setAttribute(KEY, name);
        return "redis session saved";
    }

    @GetMapping("/test")
    public String testValue(HttpSession session) throws Exception {
        return (String) session.getAttribute(KEY);
    }

    @PostMapping("/login")
    public BaseResponse<Boolean> emailLogin(HttpSession session, @RequestBody AuthRequestDTO.LoginRequest dto) {
        return new BaseResponse<Boolean>(authService.emailLogin(session, dto));
    }
}
