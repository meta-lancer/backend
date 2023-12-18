package com.metalancer.backend.creators.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.InvalidParamException;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.creators.domain.CreatorAssetList;
import com.metalancer.backend.creators.domain.ManageAsset;
import com.metalancer.backend.creators.domain.PaymentInfoManagement;
import com.metalancer.backend.creators.dto.CreatorRequestDTO;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetUpdateWithOutThumbnail;
import com.metalancer.backend.creators.dto.CreatorResponseDTO;
import com.metalancer.backend.creators.dto.CreatorResponseDTO.AssetUpdatedResponse;
import com.metalancer.backend.creators.service.CreatorReadService;
import com.metalancer.backend.creators.service.CreatorService;
import com.metalancer.backend.users.domain.Portfolio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

@Tag(name = "내가 크리에이터인 경우 조회", description = "크리에이터 조회 시, 윗부분(크리에이터 정보)와 아랫 부분(에셋, 포트폴리오) api를 분리했습니다.")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/creator")
public class MyCreatorsController {

    private final CreatorService creatorService;
    private final CreatorReadService creatorReadService;

    @Operation(summary = "에셋 등록", description = "상세페이지에 있는 모든 데이터 기반으로 구현")
    @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = CreatorResponseDTO.AssetCreatedResponse.class)))
    @PostMapping
    public BaseResponse<CreatorResponseDTO.AssetCreatedResponse> createAsset(
        @RequestPart(value = "thumbnails", required = false) MultipartFile[] thumbnails,
        @RequestPart(value = "views", required = false) MultipartFile[] views,
        @RequestPart(required = true) CreatorRequestDTO.AssetRequest dto,
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("에셋 등록 DTO - {}", dto);
        return new BaseResponse<CreatorResponseDTO.AssetCreatedResponse>(
            creatorService.createAsset(user.getUser(), thumbnails, views, dto));
    }

    @Operation(summary = "에셋 수정(url버전)", description = "썸네일 업로드 다 하고나서 순서대로 url만 보내주시면 됩니다. 만약, 보낸 이미지들이 없으면 업데이트 하지 않습니다(수정된 썸네일이 없으면 보낼지 안 보낼지는 편하게 알아서 해주시면 됩니다.)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = CreatorResponseDTO.AssetUpdatedResponse.class))),
        @ApiResponse(responseCode = "422 - E004", description = "등록자만 접근 가능", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @PatchMapping("/{productsId}")
    public BaseResponse<AssetUpdatedResponse> updateAsset(
        @PathVariable Long productsId,
        @RequestBody(required = true) CreatorRequestDTO.AssetUpdate dto,
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("에셋 수정(url) DTO - {}", dto);
        return new BaseResponse<AssetUpdatedResponse>(
            creatorService.updateAsset(productsId, user.getUser(), dto));
    }

    @Operation(summary = "에셋 수정(file버전)", description = "만약, 보낸 이미지들이 없으면 업데이트 하지 않습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = CreatorResponseDTO.AssetUpdatedResponse.class))),
        @ApiResponse(responseCode = "422 - E004", description = "등록자만 접근 가능", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @PatchMapping("/{productsId}/file")
    public BaseResponse<AssetUpdatedResponse> updateAssetWithFile(
        @PathVariable Long productsId,
        @RequestPart(value = "thumbnails", required = false) MultipartFile[] thumbnails,
        @RequestPart(required = true) AssetUpdateWithOutThumbnail dto,
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("에셋 수정(file) DTO - {}", dto);
        return new BaseResponse<AssetUpdatedResponse>(
            creatorService.updateAssetWithFile(thumbnails, productsId, user.getUser(), dto));
    }

    @Operation(summary = "에셋 파일 presignedUrl 획득", description = "유효시간 5분")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/{productsId}/asset-file/url")
    public BaseResponse<String> getAssetFilePreSignedUrl(
        @Parameter(description = "상품 고유번호") @PathVariable Long productsId) {
        return new BaseResponse<>(creatorService.getAssetFilePreSignedUrl(productsId));
    }

    @Operation(summary = "썸네일 presignedUrl 획득", description = "유효시간 5분")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400 - G008", description = "올바르지 않은 확장자", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @GetMapping("/{productsId}/thumbnail/url")
    public BaseResponse<String> getThumbnailPreSignedUrl(
        @Parameter(description = "상품 고유번호") @PathVariable Long productsId,
        @Parameter(description = "파일 확장자(\"jpg\", \"png\", \"jpeg\", \"JPG\", \"PNG\", \"JPEG\" 유효성 검사)") @RequestParam String extension) {
        if (!checkImageTypeValidate(extension)) {
            throw new InvalidParamException(ErrorCode.INVALID_EXTENSION);
        }
        return new BaseResponse<>(creatorService.getThumbnailPreSignedUrl(productsId, extension));
    }

    @Operation(summary = "에셋 등록 성공", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @PatchMapping("/{productsId}/success")
    public BaseResponse<Boolean> successAsset(
        @PathVariable Long productsId,
        @AuthenticationPrincipal PrincipalDetails user) {

        return new BaseResponse<Boolean>(
            creatorService.successAsset(productsId, user));
    }

    @Operation(summary = "에셋 등록 실패", description = "본인이 아니면 403 예외")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @DeleteMapping("/{productsId}/fail")
    public BaseResponse<Boolean> failAsset(
        @PathVariable Long productsId,
        @AuthenticationPrincipal PrincipalDetails user) {

        return new BaseResponse<Boolean>(
            creatorService.failAsset(productsId, user));
    }

    @Operation(summary = "에셋 등록 삭제", description = "본인이 아니면 403 예외")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @DeleteMapping("/{productsId}")
    public BaseResponse<Boolean> deleteAsset(
        @PathVariable Long productsId,
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<Boolean>(
            creatorService.deleteAsset(productsId, user));
    }

    @Operation(summary = "내가 등록한 에셋 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = CreatorAssetList.class)))
    })
    @GetMapping("/my-assets")
    public BaseResponse<Page<CreatorAssetList>> getMyRegisteredAssets(
        @AuthenticationPrincipal PrincipalDetails user,
        Pageable pageable) {
        pageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<Page<CreatorAssetList>>(
            creatorReadService.getMyRegisteredAssets(user, pageable));
    }

    @Operation(summary = "내 포트폴리오 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Portfolio.class)))
    })
    @GetMapping("/portfolio")
    public BaseResponse<List<Portfolio>> getMyPortfolio(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<List<Portfolio>>(creatorReadService.getMyPortfolio(user));
    }

    @Operation(summary = "내 포트폴리오 등록", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Portfolio.class)))
    })
    @PostMapping("/portfolio")
    public BaseResponse<List<Portfolio>> createMyPortfolio(
        @RequestPart(value = "files", required = false) MultipartFile[] files,
        @RequestPart CreatorRequestDTO.PortfolioCreate dto,
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<List<Portfolio>>(
            creatorService.createMyPortfolio(files, dto, user));
    }

    @Operation(summary = "내 포트폴리오 수정", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Portfolio.class)))
    })
    @PatchMapping("/portfolio/{portfolioId}")
    public BaseResponse<List<Portfolio>> updateMyPortfolio(
        @PathVariable Long portfolioId,
        @RequestPart(value = "files", required = false) MultipartFile[] files,
        @RequestPart CreatorRequestDTO.PortfolioUpdate dto,
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<List<Portfolio>>(
            creatorService.updateMyPortfolio(files, portfolioId, dto, user));
    }


    @Operation(summary = "내 포트폴리오 삭제", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Portfolio.class)))
    })
    @DeleteMapping("/portfolio/{portfolioId}")
    public BaseResponse<List<Portfolio>> deleteMyPortfolio(
        @PathVariable Long portfolioId,
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<List<Portfolio>>(
            creatorService.deleteMyPortfolio(portfolioId, user));
    }

    @Operation(summary = "에셋 관리 목록 조회", description = "파라미터에 page=1&size=5&sort=createdAt,desc 처럼 붙여주셔야 최근 등록일 순으로 정렬됩니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = ManageAsset.class)))
    })
    @GetMapping("/management")
    public BaseResponse<Page<ManageAsset>> getMyManageAssetList(
        @AuthenticationPrincipal PrincipalDetails user,
        Pageable pageable) {
        pageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<Page<ManageAsset>>(
            creatorReadService.getMyManageAssetList(user, pageable));
    }

    public boolean checkImageTypeValidate(String fileType) {
        String[] imageTypeArr = {"jpg", "png", "jpeg", "JPG", "PNG", "JPEG"};
        List<String> strList = new ArrayList<>(Arrays.asList(imageTypeArr));
        return strList.contains(fileType);
    }

    @Operation(summary = "결제정보 관리 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = ManageAsset.class)))
    })
    @GetMapping("/payment-info/management")
    public BaseResponse<PaymentInfoManagement> getMyPaymentInfoManagement(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<PaymentInfoManagement>(
            creatorReadService.getMyPaymentInfoManagement(user));
    }

    @Operation(summary = "결제정보 관리 등록", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Portfolio.class)))
    })
    @PostMapping("/payment-info/management")
    public BaseResponse<Boolean> createMyPaymentInfoManagement(
        @RequestPart(value = "idCardCopyFile", required = true) MultipartFile idCardCopyFile,
        @RequestPart(value = "accountCopyFile", required = true) MultipartFile accountCopyFile,
        @RequestPart CreatorRequestDTO.MyPaymentInfoManagementCreate dto,
        @AuthenticationPrincipal PrincipalDetails user) throws IOException {
        return new BaseResponse<Boolean>(
            creatorService.createMyPaymentInfoManagement(idCardCopyFile, accountCopyFile, dto,
                user));
    }

    @Operation(summary = "결제정보 관리 수정", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Portfolio.class)))
    })
    @PatchMapping("/payment-info/management")
    public BaseResponse<Boolean> updateMyPaymentInfoManagement(
        @RequestPart(value = "idCardCopyFile", required = false) MultipartFile idCardCopyFile,
        @RequestPart(value = "accountCopyFile", required = false) MultipartFile accountCopyFile,
        @RequestPart CreatorRequestDTO.MyPaymentInfoManagementUpdate dto,
        @AuthenticationPrincipal PrincipalDetails user) throws IOException {
        return new BaseResponse<Boolean>(
            creatorService.updateMyPaymentInfoManagement(idCardCopyFile, accountCopyFile, dto,
                user));
    }

    @Operation(summary = "결제정보 관리 삭제", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Portfolio.class)))
    })
    @DeleteMapping("/payment-info/management")
    public BaseResponse<Boolean> deleteMyPaymentInfoManagement(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<Boolean>(
            creatorService.deleteMyPaymentInfoManagement(user));
    }
}
