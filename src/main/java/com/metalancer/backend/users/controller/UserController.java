package com.metalancer.backend.users.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @Operation(summary = "판매자 전환", description = "호출하면 바로 판매자로 전환이 됩니다.")
    @ApiResponse(responseCode = "200", description = "전환 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/creator")
    public BaseResponse<Boolean> updateToCreator(
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("로그인되어있는 유저: {}", user);
        return new BaseResponse<>(userService.updateToCreator(user));
    }

    @Operation(summary = "유저의 구매한 에셋 목록 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/assets")
    public BaseResponse<Page<PayedAssets>> getPayedAssetList(
        @AuthenticationPrincipal PrincipalDetails user, Pageable pageable) {
        Pageable adjustedPageable = PageFunction.convertToOneBasedPageableDescending(pageable);
        log.info("로그인되어있는 유저: {}", user);
        return new BaseResponse<>(
            userService.getPayedAssetList(user.getUser(), adjustedPageable));
    }
}
