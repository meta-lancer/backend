package com.metalancer.backend.user.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.user.dto.AuthResponseDTO;
import com.metalancer.backend.user.dto.UserRequestDTO;
import com.metalancer.backend.user.entity.User;
import com.metalancer.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public BaseResponse<Long> createUser(@RequestBody UserRequestDTO.CreateRequest dto) throws Exception {
        return new BaseResponse<Long>(userService.createUser(dto));
    }

    @PatchMapping("/status/{link}")
    public BaseResponse<User> approveUserByLink(@PathVariable("link") String link) {
        return new BaseResponse<>(userService.approveUserByLink(link));
    }

    @GetMapping("/info")
    public BaseResponse<AuthResponseDTO.userInfo> getUserInfo(@AuthenticationPrincipal PrincipalDetails user) {
        log.info("로그인되어있는 유저: {}", user);
        return new BaseResponse<>(new AuthResponseDTO.userInfo(user.getUser()));
    }

}
