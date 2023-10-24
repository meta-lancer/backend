package com.metalancer.backend.request.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.request.domain.ProductsRequest;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Create;
import com.metalancer.backend.request.dto.ProductsRequestDTO.File;
import com.metalancer.backend.request.dto.ProductsRequestDTO.Update;
import com.metalancer.backend.request.service.RequestService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "제작요청", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/request")
public class RequestController {

    private final RequestService requestService;

    @Operation(summary = "제작요청 게시판 - 게시판 목록", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = ProductsRequest.class)))
    })
    @GetMapping("/list")
    public BaseResponse<Page<ProductsRequest>> getProductsRequestList(
        @Parameter(name = "인기분류", description =
            "VRCHAT(VR CHAT), ROBLOX(로블룩스), MINECRAFT(마인크래프트), ZEPETO(제페토), "
                + "FACIAL(페이셜 제작), MAP(맵제작), AVATAR(아바타 제작), ETC(기타)")
        @RequestParam List<String> requestTypeOptions,
        Pageable pageable) {
        Pageable adjustedPageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(
            requestService.getProductsRequestList(requestTypeOptions, adjustedPageable));
    }

    @Operation(summary = "제작요청 게시판 - 게시판 등록", description = "파일 등록 하지않는다면 fileName, fileUrl null")
    @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = ProductsRequest.class)))
    @PostMapping
    public BaseResponse<ProductsRequest> createRequest(
        @AuthenticationPrincipal PrincipalDetails user, @RequestBody Create dto) {
        return new BaseResponse<>(
            requestService.createRequest(user, dto));
    }

    @Operation(summary = "제작요청 게시판 - 게시판 수정", description = "파일 등록 하지않거나 삭제한다면 fileName, fileUrl null. 기존 유지는 기존 url, 파일명")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = ProductsRequest.class)))
    @PatchMapping("/{requestId}")
    public BaseResponse<ProductsRequest> updateRequest(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody Update dto,
        @PathVariable(name = "제작요청 게시판 고유번호") Long requestId
    ) {
        return new BaseResponse<>(
            requestService.updateRequest(user, dto, requestId));
    }

    @Operation(summary = "제작요청 게시판 - 게시판 삭제", description = "")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @DeleteMapping("/{requestId}")
    public BaseResponse<Boolean> deleteRequest(
        @AuthenticationPrincipal PrincipalDetails user,
        @PathVariable(name = "제작요청 게시판 고유번호") Long requestId
    ) {
        return new BaseResponse<>(
            requestService.deleteRequest(user, requestId));
    }

    @Operation(summary = "제작요청 게시판 - 게시판 상세", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ProductsRequest.class)))
    @GetMapping("/{requestId}")
    public BaseResponse<ProductsRequest> getRequestDetail(
        @AuthenticationPrincipal PrincipalDetails user,
        @PathVariable(name = "제작요청 게시판 고유번호") Long requestId) {
        return new BaseResponse<>(
            requestService.getRequestDetail(user, requestId));
    }

    @Operation(summary = "제작요청 게시판 - 게시판 등록 시, 파일 업로드 url", description = "")
    @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/{requestId}/presigned-url")
    public BaseResponse<String> getUploadRequestFilePreSignedUrl(
        @AuthenticationPrincipal PrincipalDetails user,
        @PathVariable(name = "제작요청 게시판 고유번호") Long requestId,
        @Parameter(name = "파일명") @RequestParam String fileName
    ) {
        return new BaseResponse<>(
            requestService.getUploadRequestFilePreSignedUrl(user, requestId, fileName));
    }

    @Operation(summary = "제작요청 게시판 - 파일 업로드 성공 시, 파일 정보 반영", description = "")
    @ApiResponse(responseCode = "200", description = "반영 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @PatchMapping("/{requestId}/file")
    public BaseResponse<Boolean> updateRequestFile(
        @AuthenticationPrincipal PrincipalDetails user,
        @PathVariable(name = "제작요청 게시판 고유번호") Long requestId,
        @RequestBody File dto
    ) {
        return new BaseResponse<>(
            requestService.updateRequestFile(user, requestId, dto));
    }
}
