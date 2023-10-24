package com.metalancer.backend.interests.controller;


import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.interests.domain.Interests;
import com.metalancer.backend.interests.service.InterestsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관심 분야", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/interests")
public class InterestsController {

    private final InterestsService interestsService;

    @Operation(summary = "관심분야 목록 조회", description = "회원가입, 유저 정보 등에 사용")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = Interests.class)))
    })
    @GetMapping
    public BaseResponse<List<Interests>> getInterests() {
        return new BaseResponse<>(interestsService.getInterests());
    }

}
