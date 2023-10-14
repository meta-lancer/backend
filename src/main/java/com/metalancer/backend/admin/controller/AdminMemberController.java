package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.domain.CreatorList;
import com.metalancer.backend.admin.domain.MemberList;
import com.metalancer.backend.admin.domain.RegisterList;
import com.metalancer.backend.admin.dto.AdminMemberDTO;
import com.metalancer.backend.admin.service.AdminMemberService;
import com.metalancer.backend.common.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PatchMapping("/approve")
    public BaseResponse<String> approveMemberList(@RequestBody AdminMemberDTO.Approve dto) {
        return new BaseResponse<String>(adminMemberService.approveUserList(dto));
    }

    @PatchMapping
    public BaseResponse<MemberList> updateMember(@RequestBody AdminMemberDTO.UpdateUser dto) {
        return new BaseResponse<MemberList>(adminMemberService.updateMember(dto));
    }
}
