package com.metalancer.backend.member.controller;


import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/kakao")
    public BaseResponse<Boolean> kakaoLogin(@RequestParam("code") String code) throws Exception {
        authService.kakaoLogin(code);
        return new BaseResponse<Boolean>(true);
    }

}
