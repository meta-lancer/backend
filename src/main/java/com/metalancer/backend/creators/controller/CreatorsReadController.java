package com.metalancer.backend.creators.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.creators.domain.CreatorAssetList;
import com.metalancer.backend.creators.service.CreatorReadService;
import com.metalancer.backend.users.domain.Portfolio;
import com.metalancer.backend.users.dto.UserResponseDTO;
import com.metalancer.backend.users.dto.UserResponseDTO.IntroAndCareer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "다른 크리에이터 조회", description = "크리에이터 조회 시, 윗부분(크리에이터 정보)와 아랫 부분(에셋, 포트폴리오) api를 분리했습니다.")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CreatorsReadController {

    private final CreatorReadService creatorReadService;

    @Operation(summary = "기존 정보 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserResponseDTO.OtherCreatorBasicInfo.class)))
    @GetMapping("/users/{creatorId}/basic")
    public BaseResponse<UserResponseDTO.OtherCreatorBasicInfo> getBasicInfo(
        @AuthenticationPrincipal PrincipalDetails user,
        @Parameter(description = "크리에이터 고유번호") @PathVariable Long creatorId) {
        return new BaseResponse<UserResponseDTO.OtherCreatorBasicInfo>(
            creatorReadService.getCreatorBasicInfo(user, creatorId));
    }

    @Operation(summary = "소개 및 업무경험 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = IntroAndCareer.class)))
    @GetMapping("/users/{creatorId}/career")
    public BaseResponse<IntroAndCareer> getIntroAndCareer(
        @AuthenticationPrincipal PrincipalDetails user,
        @Parameter(description = "크리에이터 고유번호") @PathVariable Long creatorId) {
        return new BaseResponse<IntroAndCareer>(
            creatorReadService.getCreatorIntroAndCareer(user, creatorId));
    }

    @Operation(summary = "크리에이터 에셋 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = CreatorAssetList.class)))
    })
    @GetMapping("/users/{creatorId}/creator-asset")
    public BaseResponse<Page<CreatorAssetList>> getCreatorAssetList(
        @AuthenticationPrincipal PrincipalDetails user,
        @Parameter(description = "크리에이터 고유번호") @PathVariable Long creatorId,
        Pageable pageable) {
        pageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(
            creatorReadService.getCreatorAssetList(user, creatorId, pageable));
    }

    @Operation(summary = "크리에이터 포트폴리오 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Portfolio.class)))
    })
    @GetMapping("/users/{creatorId}/portfolio")
    public BaseResponse<List<Portfolio>> getCreatorPortfolio(
        @Parameter(description = "크리에이터 고유번호") @PathVariable Long creatorId) {
        return new BaseResponse<>(creatorReadService.getCreatorPortfolio(creatorId));
    }
}
