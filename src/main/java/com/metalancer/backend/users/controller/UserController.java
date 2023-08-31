package com.metalancer.backend.users.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저정보 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/info")
    public BaseResponse<AuthResponseDTO.userInfo> getUserInfo(
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("로그인되어있는 유저: {}", user);
        return new BaseResponse<>(new AuthResponseDTO.userInfo(user.getUser()));
    }


}
