package com.metalancer.backend.users.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.creators.dto.CreatorRequestDTO;
import com.metalancer.backend.users.domain.OrderStatusList;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.domain.PayedOrder;
import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.dto.UserRequestDTO;
import com.metalancer.backend.users.dto.UserResponseDTO;
import com.metalancer.backend.users.dto.UserResponseDTO.IntroAndCareer;
import com.metalancer.backend.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "유저", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저정보 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = AuthResponseDTO.userInfo.class)))
    @GetMapping("/info")
    public BaseResponse<AuthResponseDTO.userInfo> getUserInfo(
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("로그인되어있는 유저: {}", user);
        validateUserAuthentication(user);
        return new BaseResponse<AuthResponseDTO.userInfo>(userService.getUserInfo(user));
    }

//    @Operation(summary = "판매자 전환", description = "호출하면 바로 판매자로 전환이 됩니다.")
//    @ApiResponse(responseCode = "200", description = "전환 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
//    @PatchMapping("/creator")
//    public BaseResponse<Boolean> updateToCreator(
//            @AuthenticationPrincipal PrincipalDetails user) {
//        log.info("로그인되어있는 유저: {}", user);
//        validateUserAuthentication(user);
//        return new BaseResponse<>(userService.updateToCreator(user));
//    }

    @Operation(summary = "마이페이지 - 기존 정보 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserResponseDTO.BasicInfo.class)))
    @GetMapping("/basic")
    public BaseResponse<UserResponseDTO.BasicInfo> getBasicInfo(
        @AuthenticationPrincipal PrincipalDetails user) {
        validateUserAuthentication(user);
        return new BaseResponse<UserResponseDTO.BasicInfo>(userService.getBasicInfo(user));
    }

    @Operation(summary = "마이페이지 - 기존 정보 조회 수정", description = "")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = UserResponseDTO.BasicInfo.class)))
    @PatchMapping("/basic")
    public BaseResponse<UserResponseDTO.BasicInfo> updateBasicInfo(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody UserRequestDTO.UpdateBasicInfo dto
    ) {
        validateUserAuthentication(user);
        return new BaseResponse<UserResponseDTO.BasicInfo>(
            userService.updateBasicInfo(user, dto));
    }


    @Operation(summary = "마이페이지 - 소개 및 업무경험 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = IntroAndCareer.class)))
    @GetMapping("/career")
    public BaseResponse<IntroAndCareer> getIntroAndCareer(
        @AuthenticationPrincipal PrincipalDetails user) {
        validateUserAuthentication(user);
        return new BaseResponse<IntroAndCareer>(
            userService.getIntroAndCareer(user));
    }

    @Operation(summary = "마이페이지 - 소개 및 업무경험 수정(자기소개만 수정)", description = "")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = IntroAndCareer.class)))
    @PatchMapping("/career/intro")
    public BaseResponse<IntroAndCareer> updateIntro(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody UserRequestDTO.UpdateCareerIntroRequest dto
    ) {
        validateUserAuthentication(user);
        return new BaseResponse<IntroAndCareer>(
            userService.updateCareerIntro(user, dto));
    }

    @Operation(summary = "마이페이지 - 업무경험 등록", description = "")
    @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = IntroAndCareer.class)))
    @PostMapping("/career")
    public BaseResponse<IntroAndCareer> createCareer(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody UserRequestDTO.CreateCareerRequest dto) {
        validateUserAuthentication(user);
        return new BaseResponse<IntroAndCareer>(
            userService.createCareer(user, dto));
    }

    @Operation(summary = "마이페이지 - 업무경험 수정", description = "")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = IntroAndCareer.class)))
    @PatchMapping("/career/{careerId}")
    public BaseResponse<IntroAndCareer> updateCareer(
        @PathVariable Long careerId,
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody UserRequestDTO.UpdateCareerRequest dto) {
        validateUserAuthentication(user);
        return new BaseResponse<IntroAndCareer>(
            userService.updateCareer(careerId, user, dto));
    }

    @Operation(summary = "마이페이지 - 업무경험 삭제", description = "")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = IntroAndCareer.class)))
    @DeleteMapping("/career/{careerId}")
    public BaseResponse<IntroAndCareer> deleteCareer(
        @PathVariable Long careerId,
        @AuthenticationPrincipal PrincipalDetails user) {
        validateUserAuthentication(user);
        return new BaseResponse<IntroAndCareer>(
            userService.deleteCareer(careerId, user));
    }

    @Operation(summary = "마이페이지 - 유저 결제 목록 조회", description = "beginDate, endDate 2023.07.13 형식으로 보내주세요. title과 receiptUrl은 새로 추가되서 데이터가 없는걸로 나올 겁니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = PayedOrder.class)))
    })
    @GetMapping("/payment/list")
    public BaseResponse<Page<PayedOrder>> getPaymentList(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestParam(value = "status", required = false) String status,
        @RequestParam("beginDate") String beginDate,
        @RequestParam("endDate") String endDate,
        Pageable pageable) {
        Pageable adjustedPageable = PageFunction.convertToOneBasedPageableDescending(pageable);
        log.info("로그인되어있는 유저: {}", user);
        validateUserAuthentication(user);
        return new BaseResponse<Page<PayedOrder>>(
            userService.getPaymentList(user, status, beginDate, endDate, adjustedPageable));
    }

    @Operation(summary = "마이페이지 - 구매 관리(유저의 구매한 에셋 목록 조회)", description = "구매 상태 api 활용. 전체 상태는 그냥 status=로 보내면 됩니다. 혹은 status=PAY_DONE 처럼")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = PayedAssets.class)))
    })
    @GetMapping("/payment/assets")
    public BaseResponse<Page<PayedAssets>> getPayedAssetList(
        @RequestParam(value = "status") String status,
        @RequestParam("beginDate") String beginDate,
        @RequestParam("endDate") String endDate,
        @AuthenticationPrincipal PrincipalDetails user, Pageable pageable) {
        pageable = PageFunction.convertToOneBasedPageableDescending(pageable);
        log.info("로그인되어있는 유저: {}", user);
        validateUserAuthentication(user);
        return new BaseResponse<Page<PayedAssets>>(
            userService.getPayedAssetList(status, beginDate, endDate, user, pageable));
    }

    @Operation(summary = "마이페이지 - 구매 상태", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = OrderStatusList.class)))
    })
    @GetMapping("/payment/status")
    public BaseResponse<List<OrderStatusList>> getOrderStatusList() {
        return new BaseResponse<List<OrderStatusList>>(
            userService.getOrderStatusList());
    }

    public void validateUserAuthentication(PrincipalDetails user) {
        if (user == null) {
            throw new BaseException(ErrorCode.LOGIN_REQUIRED);
        }
    }

    @Operation(summary = "마이페이지 - 크리에이터 전환 신청", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Boolean.class)))
    })
    @PostMapping("/portfolio")
    public BaseResponse<Boolean> applyCreator(
        @RequestPart(value = "files", required = false) MultipartFile[] files,
        @RequestPart CreatorRequestDTO.ApplyCreator dto,
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<Boolean>(
            userService.applyCreator(files, dto, user));
    }

    @Operation(summary = "마이페이지 - 구매 관리 문의 등록", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Boolean.class)))
    })
    @PostMapping("/inquiry")
    public BaseResponse<Boolean> createInquiry(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody UserRequestDTO.CreateInquiryRequest dto) {
        return new BaseResponse<Boolean>(
            userService.createInquiry(user, dto));
    }
}
