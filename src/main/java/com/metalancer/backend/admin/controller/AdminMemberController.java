package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.dto.CreatorList;
import com.metalancer.backend.admin.dto.MemberList;
import com.metalancer.backend.admin.dto.RegisterList;
import com.metalancer.backend.admin.service.AdminMemberService;
import com.metalancer.backend.common.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/member")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping("/list")
    public BaseResponse<List<MemberList>> getAdminMemberList() {
        return new BaseResponse<List<MemberList>>(adminMemberService.getAdminMemberList());
    }

    @GetMapping("/register")
    public BaseResponse<List<RegisterList>> getAdminRegisterList() {
        return new BaseResponse<List<RegisterList>>(adminMemberService.getAdminRegisterList());
    }

    @GetMapping("/creator")
    public BaseResponse<List<CreatorList>> getAdminCreatorList() {
        return new BaseResponse<List<CreatorList>>(adminMemberService.getAdminCreatorList());
    }

}
