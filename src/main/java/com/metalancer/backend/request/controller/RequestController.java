package com.metalancer.backend.request.controller;


import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.request.domain.ProductsRequest;
import com.metalancer.backend.request.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "제작요청", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/request")
public class RequestController {

    private final RequestService requestService;

    @Operation(summary = "제작요청 게시판 - 게시판 목록", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
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

}
