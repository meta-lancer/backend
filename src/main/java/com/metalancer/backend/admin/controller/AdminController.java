package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.service.AdminService;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.users.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public BaseResponse<String> getAdminTest() throws Exception {
        return new BaseResponse<String>("admin입니다.");
    }

    @Operation(summary = "어드민 여부 체크", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserResponseDTO.OtherCreatorBasicInfo.class)))
    @GetMapping("/role")
    public BaseResponse<Boolean> checkIfAdmin(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<Boolean>(
            adminService.checkIfAdmin(user));
    }
}
