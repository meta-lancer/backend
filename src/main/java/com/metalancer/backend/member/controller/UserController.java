package com.metalancer.backend.member.controller;


import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.member.dto.MemberRequestDTO;
import com.metalancer.backend.member.entity.User;
import com.metalancer.backend.member.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public BaseResponse<Long> createUser(@RequestBody MemberRequestDTO.CreateRequest dto) throws Exception {
        return new BaseResponse<Long>(userService.createUser(dto));
    }

    @PatchMapping("/status/{link}")
    public BaseResponse<User> approveUserByLink(@PathVariable("link") String link) {
        return new BaseResponse<>(userService.approveUserByLink(link));
    }

}
