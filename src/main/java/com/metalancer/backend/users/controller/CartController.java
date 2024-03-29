package com.metalancer.backend.users.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.AuthUtils;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.users.domain.Cart;
import com.metalancer.backend.users.dto.UserRequestDTO.CreateCartRequest;
import com.metalancer.backend.users.dto.UserRequestDTO.DeleteCartRequest;
import com.metalancer.backend.users.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Cart.class)))
    })
    @GetMapping
    public BaseResponse<Page<Cart>> getAllCart(
        @AuthenticationPrincipal PrincipalDetails user,
        @Parameter(description = "페이징") Pageable pageable) {
        log.info("로그인되어있는 유저: {}", user);
        AuthUtils.validateUserAuthentication(user);
        Pageable adjustedPageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(cartService.getAllCart(user.getUser(), adjustedPageable));
    }

    @Operation(summary = "장바구니 생성", description = "")
    @ApiResponse(responseCode = "200", description = "생성 성공. true면 새롭게 생성, false면 이미 존재함", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @PostMapping("/asset")
    public BaseResponse<Boolean> createCart(
        @AuthenticationPrincipal PrincipalDetails user,
        @Parameter @RequestBody CreateCartRequest dto) {
        log.info("로그인되어있는 유저: {}", user);
        log.info("장바구니 생성: {}", dto);
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<>(
            cartService.createCart(user.getUser(), dto));
    }

    @Operation(summary = "장바구니 삭제", description = "")
    @ApiResponse(responseCode = "200", description = "삭제 성공. true면 삭제 완료, false면 애초에 존재 x", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @DeleteMapping("/asset")
    public BaseResponse<Boolean> deleteCart(
        @AuthenticationPrincipal PrincipalDetails user,
        @Parameter @RequestBody DeleteCartRequest dto) {
        log.info("로그인되어있는 유저: {}", user);
        log.info("장바구니 삭제: {}", dto);
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<>(
            cartService.deleteCart(user.getUser(), dto));
    }

    @Operation(summary = "장바구니 전체 삭제", description = "")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @DeleteMapping
    public BaseResponse<Boolean> deleteAllCart(
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("로그인되어있는 유저: {}", user);
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<>(cartService.deleteAllCart(user.getUser()));
    }

}
