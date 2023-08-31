package com.metalancer.backend.creators.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.creators.service.CreatorReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "크리에이터 조회", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CreatorsReadController {

    private final CreatorReadService creatorReadService;

    @Operation(summary = "크리에이터 정보 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/users/{creatorId}/creator-info")
    public BaseResponse<String> getCreatorInfo(
        @AuthenticationPrincipal PrincipalDetails user,
        @Parameter(description = "크리에이터 고유번호") @PathVariable Long creatorId) {
        return new BaseResponse<>(creatorReadService.getCreatorInfo(user, creatorId));
    }

    @Operation(summary = "크리에이터 에셋 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/users/{creatorId}/creator-asset")
    public BaseResponse<String> getCreatorAssetList(
        @AuthenticationPrincipal PrincipalDetails user,
        @Parameter(description = "크리에이터 고유번호") @PathVariable Long creatorId) {
        return new BaseResponse<>(creatorReadService.getCreatorAssetList(user, creatorId));
    }

    @Operation(summary = "크리에이터 포트폴리오 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/users/{creatorId}/creator-portfolio")
    public BaseResponse<String> getCreatorPortfolio(
        @Parameter(description = "크리에이터 고유번호") @PathVariable Long creatorId) {
        return new BaseResponse<>(creatorReadService.getCreatorPortfolio(creatorId));
    }
}
