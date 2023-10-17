package com.metalancer.backend.creators.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.creators.dto.CreatorRequestDTO;
import com.metalancer.backend.creators.dto.CreatorResponseDTO;
import com.metalancer.backend.creators.service.CreatorService;
import com.metalancer.backend.users.repository.UserRepository;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "판매자", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
/// 잠시 creators -> creator
@RequestMapping("/api/creator")
public class CreatorsController {

    private final CreatorService creatorService;
    private final UserRepository userRepository;

    @Operation(summary = "에셋 등록", description = "미구현")
    @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping
    public BaseResponse<CreatorResponseDTO.AssetCreatedResponse> createAsset(
//        @AuthenticationPrincipal
//        PrincipalDetails user,
        @RequestPart(value = "thumbnails", required = false) MultipartFile[] thumbnails,
        @RequestPart(value = "views", required = false) MultipartFile[] views,
        @RequestPart(value = "zipFile", required = false) MultipartFile zipFile,
        @RequestPart(required = true) CreatorRequestDTO.AssetRequest dto,
        @AuthenticationPrincipal PrincipalDetails user) {

        return new BaseResponse<CreatorResponseDTO.AssetCreatedResponse>(
            creatorService.createAsset(user.getUser(), thumbnails, views, zipFile, dto));
    }

    @Operation(summary = "에셋 파일 presignedUrl 획득", description = "유효시간 5분")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/{productsId}/asset-file/url")
    public BaseResponse<String> getAssetFilePreSignedUrl(
        @Parameter(description = "상품 고유번호") @PathVariable Long productsId) {
        return new BaseResponse<>(creatorService.getAssetFilePreSignedUrl(productsId));
    }
}
