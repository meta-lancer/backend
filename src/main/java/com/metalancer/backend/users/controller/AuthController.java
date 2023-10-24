package com.metalancer.backend.users.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.dto.UserRequestDTO;
import com.metalancer.backend.users.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인/회원가입", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "이메일 회원가입 - 일반 유저", description = "이메일, 비밀번호, 직업, 관심분야, 약관동의 필요. Response - 회원고유번호")
    @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = Long.class)))
    @PostMapping
    public BaseResponse<Long> createUser(@RequestBody UserRequestDTO.CreateRequest dto)
        throws Exception {
        return new BaseResponse<Long>(authService.createUser(dto));
    }

    @Operation(summary = "이메일 회원가입 - 크리에이터 가입", description = "이메일, 비밀번호, 직업, 관심분야, 약관동의 필요. Response - 크리에이터 고유번호")
    @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = Long.class)))
    @PostMapping("/creator")
    public BaseResponse<Long> createCreator(@RequestBody UserRequestDTO.CreateRequest dto)
        throws Exception {
        return new BaseResponse<Long>(authService.createCreator(dto));
    }


    @Operation(summary = "가입 승인처리", description = "")
    @ApiResponse(responseCode = "200", description = "승인 성공", content = @Content(schema = @Schema(implementation = AuthResponseDTO.userInfo.class)))
    @PatchMapping("/status/{link}")
    public BaseResponse<AuthResponseDTO.userInfo> approveUserByLink(
        @PathVariable("link") String link) {
        return new BaseResponse<>(authService.approveUserByLink(link));
    }

    @Hidden
    @GetMapping("/test")
    public Object testValue(HttpSession session, @RequestParam String key,
        @AuthenticationPrincipal PrincipalDetails user) throws Exception {
        log.info("로그인되어있는 유저: {}", session.getAttribute(key));
        log.info("로그인되어있는 유저: {}", user);
        return session.getAttribute(key);
    }

//    @Operation(summary = "이메일 로그인", description = "")
//    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
//    @PostMapping("/login")
//    public BaseResponse<Boolean> emailLogin(HttpSession session,
//        @RequestBody AuthRequestDTO.LoginRequest dto) {
//        return new BaseResponse<Boolean>(authService.emailLogin(session, dto));
//    }

//    @Operation(summary = "이메일 로그아웃", description = "")
//    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
//    @PostMapping("/logout")
//    public BaseResponse<Boolean> emailLogout(HttpSession session) {
//        return new BaseResponse<Boolean>(authService.emailLogout(session));
//    }


}
