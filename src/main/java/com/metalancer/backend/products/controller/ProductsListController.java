package com.metalancer.backend.products.controller;


import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.products.domain.FilterAsset;
import com.metalancer.backend.products.dto.ProductsDto.GenreGalaxyResponse;
import com.metalancer.backend.products.dto.ProductsDto.HotPickResponse;
import com.metalancer.backend.products.dto.ProductsDto.TrendSpotlightResponse;
import com.metalancer.backend.products.service.ProductsListService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품 목록", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products/list")
public class ProductsListController {

    private final ProductsListService productsListService;

    @Operation(summary = "메인페이지-에셋 Hot Pick", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/hot-pick")
    public BaseResponse<HotPickResponse> getHotPickList(
        @Parameter(description = "종류") @RequestParam HotPickType type,
        @Parameter(description = "기간") @RequestParam PeriodType period,
        @Parameter(description = "페이징") Pageable pageable) {
        log.info("종류 옵션-{}, 기간 옵션-{}, 페이징-{}", type, period, pageable);
        Pageable adjustedPageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(
            productsListService.getHotPickList(type, period, adjustedPageable));
    }

    @Operation(summary = "메인페이지-Trend Spotlight", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/trend-spotlight")
    public BaseResponse<TrendSpotlightResponse> getTrendSpotlight(
        @Parameter(name = "종류", description =
            "ALL(전체), VRCHAT(VR CHAT), MINECRAFT(마인크래프트), ZEPETO(제페토), "
                + "ROBLOX(로블룩스), MIDDLEAGE(중세), FUTURE(미래), CARTOON(카툰), ACTUAL(실사)") @RequestParam String platformType,
        @Parameter(description = "페이징") Pageable pageable) {
        log.info("종류 옵션-{},  페이징-{}", platformType, pageable);
        Pageable adjustedPageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(
            productsListService.getTrendSpotlight(platformType, adjustedPageable));
    }

    @Operation(summary = "메인페이지-Genre Galaxy", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/genre-galaxy")
    public BaseResponse<GenreGalaxyResponse> getGenreGalaxyList(
        @Parameter(name = "종류", description = "MODEL(\"모델\"),\n"
            + "    ANIMAL(\"동물\"),\n"
            + "    PLANT(\"식물\"),\n"
            + "    BACKGROUND(\"배경\"),\n"
            + "    FOOD(\"음식\"),\n"
            + "    OBJECTS(\"사물\"),\n"
            + "    ROBOT(\"로봇\"),\n"
            + "    VFX(\"VFX\"),\n"
            + "    ETC(\"기타\")") @RequestParam String type,
        @Parameter(description = "페이징") Pageable pageable) {
        log.info("종류 옵션-{},  페이징-{}", type, pageable);
        Pageable adjustedPageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(productsListService.getGenreGalaxyList(type, adjustedPageable));
    }

    @Operation(summary = "모두보기-필터 에셋", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/asset")
    public BaseResponse<Page<FilterAsset>> getFilterAssetList(
        @Parameter(description = "정렬 옵션") @RequestParam Integer sortOption,
        @Parameter(description = "유형 옵션") @RequestParam List<Integer> typeOption,
        @Parameter(description = "장르 옵션") @RequestParam List<Integer> genreOption,
        @Parameter(description = "가격 옵션") @RequestParam List<Integer> priceOption,
        @Parameter(description = "페이징") Pageable pageable) {
        log.info("정렬 옵션-{}, 유형 옵션-{}, 장르 옵션-{}, 가격 옵션-{}, 페이징-{}", sortOption, typeOption,
            genreOption, priceOption, pageable);
        Pageable adjustedPageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(
            productsListService.getFilterAssetList(sortOption, typeOption, genreOption, priceOption,
                adjustedPageable));
    }
}
