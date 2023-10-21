package com.metalancer.backend.creators.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.creators.domain.CreatorAssetList;
import com.metalancer.backend.creators.domain.ManageAsset;
import com.metalancer.backend.creators.dto.CreatorRequestDTO;
import com.metalancer.backend.creators.dto.CreatorResponseDTO;
import com.metalancer.backend.creators.service.CreatorReadService;
import com.metalancer.backend.creators.service.CreatorService;
import com.metalancer.backend.users.domain.Portfolio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "내가 크리에이터인 경우 조회", description = "크리에이터 조회 시, 윗부분(크리에이터 정보)와 아랫 부분(에셋, 포트폴리오) api를 분리했습니다.")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/creator")
public class MyCreatorsController {

    private final CreatorService creatorService;
    private final CreatorReadService creatorReadService;

    @Operation(summary = "에셋 등록", description = "미구현")
    @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping
    public BaseResponse<CreatorResponseDTO.AssetCreatedResponse> createAsset(
        @RequestPart(value = "thumbnails", required = false) MultipartFile[] thumbnails,
        @RequestPart(value = "views", required = false) MultipartFile[] views,
        @RequestPart(required = true) CreatorRequestDTO.AssetRequest dto,
        @AuthenticationPrincipal PrincipalDetails user) {

        return new BaseResponse<CreatorResponseDTO.AssetCreatedResponse>(
            creatorService.createAsset(user.getUser(), thumbnails, views, dto));
    }

    @Operation(summary = "에셋 파일 presignedUrl 획득", description = "유효시간 5분")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/{productsId}/asset-file/url")
    public BaseResponse<String> getAssetFilePreSignedUrl(
        @Parameter(description = "상품 고유번호") @PathVariable Long productsId) {
        return new BaseResponse<>(creatorService.getAssetFilePreSignedUrl(productsId));
    }

    @Operation(summary = "에셋 등록 성공", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/{productsId}/success")
    public BaseResponse<Boolean> successAsset(
        @PathVariable Long productsId,
        @AuthenticationPrincipal PrincipalDetails user) {

        return new BaseResponse<Boolean>(
            creatorService.successAsset(productsId, user));
    }

    @Operation(summary = "에셋 등록 실패", description = "본인이 아니면 403 예외")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @DeleteMapping("/{productsId}/fail")
    public BaseResponse<Boolean> failAsset(
        @PathVariable Long productsId,
        @AuthenticationPrincipal PrincipalDetails user) {

        return new BaseResponse<Boolean>(
            creatorService.failAsset(productsId, user));
    }

    @Operation(summary = "에셋 등록 삭제", description = "본인이 아니면 403 예외")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @DeleteMapping("/{productsId}")
    public BaseResponse<Boolean> deleteAsset(
        @PathVariable Long productsId,
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<Boolean>(
            creatorService.deleteAsset(productsId, user));
    }

    @Operation(summary = "내가 등록한 에셋 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/my-assets")
    public BaseResponse<Page<CreatorAssetList>> getMyRegisteredAssets(
        @AuthenticationPrincipal PrincipalDetails user,
        Pageable pageable) {
        pageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<Page<CreatorAssetList>>(
            creatorReadService.getMyRegisteredAssets(user, pageable));
    }

    @Operation(summary = "내 포트폴리오 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/portfolio")
    public BaseResponse<List<Portfolio>> getMyPortfolio(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<List<Portfolio>>(creatorReadService.getMyPortfolio(user));
    }

    @Operation(summary = "내 포트폴리오 등록", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping("/portfolio")
    public BaseResponse<List<Portfolio>> createMyPortfolio(
        @RequestBody CreatorRequestDTO.PortfolioCreate dto,
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<List<Portfolio>>(
            creatorService.createMyPortfolio(dto, user));
    }

    @Operation(summary = "내 포트폴리오 수정", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/portfolio/{portfolioId}")
    public BaseResponse<List<Portfolio>> updateMyPortfolio(
        @PathVariable Long portfolioId,
        @RequestBody CreatorRequestDTO.PortfolioUpdate dto,
        @AuthenticationPrincipal PrincipalDetails user) {

        return new BaseResponse<List<Portfolio>>(
            creatorService.updateMyPortfolio(portfolioId, dto, user));
    }


    @Operation(summary = "내 포트폴리오 삭제", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @DeleteMapping("/portfolio/{portfolioId}")
    public BaseResponse<List<Portfolio>> deleteMyPortfolio(
        @PathVariable Long portfolioId,
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<List<Portfolio>>(
            creatorService.deleteMyPortfolio(portfolioId, user));
    }

    @Operation(summary = "에셋 관리 목록 조회", description = "파라미터에 page=1&size=5&sort=createdAt,desc 처럼 붙여주셔야 최근 등록일 순으로 정렬됩니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/management")
    public BaseResponse<Page<ManageAsset>> getMyManageAssetList(
        @AuthenticationPrincipal PrincipalDetails user,
        Pageable pageable) {
        pageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<Page<ManageAsset>>(
            creatorReadService.getMyManageAssetList(user, pageable));
    }
}
