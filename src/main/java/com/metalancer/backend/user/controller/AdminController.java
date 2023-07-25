package com.metalancer.backend.user.controller;


import com.metalancer.backend.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    @GetMapping
    public BaseResponse<String> getAdminTest() throws Exception {
        return new BaseResponse<String>("admin입니다.");
    }

}
