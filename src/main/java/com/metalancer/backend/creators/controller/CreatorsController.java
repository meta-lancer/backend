package com.metalancer.backend.creators.controller;


import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.creators.dto.CreatorRequestDTO;
import com.metalancer.backend.creators.dto.CreatorResponseDTO;
import com.metalancer.backend.creators.service.CreatorService;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        @RequestPart(required = true) CreatorRequestDTO.AssetRequest dto) {
        Optional<User> userEntity = userRepository.findByEmail("aaa111@naver.com");
//        if (user != null) {
//            userEntity = user.getUser();
//        }

        return new BaseResponse<CreatorResponseDTO.AssetCreatedResponse>(
            creatorService.createAsset(userEntity.get(), thumbnails, views, zipFile, dto));
    }

}
