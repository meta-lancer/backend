package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.domain.CreatorList;
import com.metalancer.backend.admin.domain.MemberList;
import com.metalancer.backend.admin.domain.RegisterList;
import com.metalancer.backend.admin.dto.AdminMemberDTO;
import com.metalancer.backend.admin.service.AdminMemberService;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.users.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{memberId}")
    public BaseResponse<User> getAdminMemberDetail(@PathVariable("memberId") Long memberId) {
        return new BaseResponse<User>(adminMemberService.getAdminMemberDetail(memberId));
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

    @PatchMapping("/{memberId}/creator")
    public BaseResponse<String> improveToCreator(@PathVariable("memberId") Long memberId) {
        return new BaseResponse<String>(adminMemberService.improveToCreator(memberId));
    }

    @DeleteMapping("/{memberId}")
    public BaseResponse<String> deleteMember(@PathVariable("memberId") Long memberId) {
        return new BaseResponse<String>(adminMemberService.deleteMember(memberId));
    }

    @PatchMapping
    public BaseResponse<MemberList> updateMember(@RequestBody AdminMemberDTO.UpdateUser dto) {
        return new BaseResponse<MemberList>(adminMemberService.updateMember(dto));
    }
}
