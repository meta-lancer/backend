package com.metalancer.backend.users.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.users.domain.PayedOrder;
import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.dto.UserRequestDTO;
import com.metalancer.backend.users.dto.UserResponseDTO;
import com.metalancer.backend.users.dto.UserResponseDTO.IntroAndCareer;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Operation(summary = "마이페이지 - 기존 정보 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/basic")
    public BaseResponse<UserResponseDTO.BasicInfo> getBasicInfo(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<UserResponseDTO.BasicInfo>(userService.getBasicInfo(user));
    }

    @Operation(summary = "마이페이지 - 기존 정보 조회 수정", description = "")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/basic")
    public BaseResponse<UserResponseDTO.BasicInfo> updateBasicInfo(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody UserRequestDTO.UpdateBasicInfo dto
    ) {
        return new BaseResponse<UserResponseDTO.BasicInfo>(
            userService.updateBasicInfo(user, dto));
    }


    @Operation(summary = "마이페이지 - 소개 및 업무경험 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/career")
    public BaseResponse<IntroAndCareer> getIntroAndCareer(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<IntroAndCareer>(
            userService.getIntroAndCareer(user));
    }

    @Operation(summary = "마이페이지 - 소개 및 업무경험 수정(자기소개만 수정)", description = "")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/career/intro")
    public BaseResponse<IntroAndCareer> updateIntro(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody UserRequestDTO.UpdateCareerIntroRequest dto
    ) {
        return new BaseResponse<IntroAndCareer>(
            userService.updateCareerIntro(user, dto));
    }

    @Operation(summary = "마이페이지 - 업무경험 등록", description = "")
    @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping("/career")
    public BaseResponse<IntroAndCareer> createCareer(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody UserRequestDTO.CreateCareerRequest dto) {
        return new BaseResponse<IntroAndCareer>(
            userService.createCareer(user, dto));
    }

    @Operation(summary = "마이페이지 - 업무경험 수정", description = "")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/career/{careerId}")
    public BaseResponse<IntroAndCareer> updateCareer(
        @PathVariable Long careerId,
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody UserRequestDTO.UpdateCareerRequest dto) {
        return new BaseResponse<IntroAndCareer>(
            userService.updateCareer(careerId, user, dto));
    }

    @Operation(summary = "마이페이지 - 업무경험 삭제", description = "")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @DeleteMapping("/career/{careerId}")
    public BaseResponse<IntroAndCareer> deleteCareer(
        @PathVariable Long careerId,
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<IntroAndCareer>(
            userService.deleteCareer(careerId, user));
    }

    @Operation(summary = "마이페이지 - 유저 결제 목록 조회", description = "beginDate, endDate 2023.07.13 형식으로 보내주세요")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/payment/list")
    public BaseResponse<Page<PayedOrder>> getPaymentList(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam("beginDate") String beginDate,
        @RequestParam("endDate") String endDate,
        Pageable pageable) {
        Pageable adjustedPageable = PageFunction.convertToOneBasedPageableDescending(pageable);
        log.info("로그인되어있는 유저: {}", user);
        return new BaseResponse<Page<PayedOrder>>(
            userService.getPaymentList(user, type, beginDate, endDate, adjustedPageable));
    }
//
//    @Operation(summary = "마이페이지 - 유저의 구매한 에셋 목록 조회", description = "")
//    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
//    @GetMapping("/assets")
//    public BaseResponse<Page<PayedAssets>> getPayedAssetList(
//        @AuthenticationPrincipal PrincipalDetails user, Pageable pageable) {
//        Pageable adjustedPageable = PageFunction.convertToOneBasedPageableDescending(pageable);
//        log.info("로그인되어있는 유저: {}", user);
//        return new BaseResponse<>(
//            userService.getPayedAssetList(user.getUser(), adjustedPageable));
//    }
}
