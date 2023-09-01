package com.metalancer.backend.users.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.users.domain.Cart;
import com.metalancer.backend.users.service.CartService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "장바구니", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping
    public BaseResponse<Page<Cart>> getAllCart(
        @AuthenticationPrincipal PrincipalDetails user,
        @Parameter(description = "페이징") Pageable pageable) {
        log.info("로그인되어있는 유저: {}", user);

        Pageable adjustedPageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(cartService.getAllCart(user.getUser(), adjustedPageable));
    }

    @Operation(summary = "장바구니 생성/삭제", description = "")
    @ApiResponse(responseCode = "200", description = "생성/삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping("/asset/{assetId}")
    public BaseResponse<Boolean> toggleCart(
        @AuthenticationPrincipal PrincipalDetails user,
        @Parameter @PathVariable Long assetId) {
        log.info("로그인되어있는 유저: {}", user);
        return new BaseResponse<>(cartService.toggleCart(user.getUser(), assetId));
    }

    @Operation(summary = "장바구니 전체 삭제", description = "")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @DeleteMapping
    public BaseResponse<Boolean> deleteAllCart(
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("로그인되어있는 유저: {}", user);
        return new BaseResponse<>(cartService.deleteAllCart(user.getUser()));
    }
}
