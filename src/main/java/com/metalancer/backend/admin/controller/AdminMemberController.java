package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.domain.CreatorList;
import com.metalancer.backend.admin.domain.MemberDetail;
import com.metalancer.backend.admin.domain.MemberList;
import com.metalancer.backend.admin.domain.RegisterList;
import com.metalancer.backend.admin.dto.AdminMemberDTO;
import com.metalancer.backend.admin.service.AdminMemberService;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public BaseResponse<List<MemberList>> getAdminMemberList(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<List<MemberList>>(adminMemberService.getAdminMemberList());
    }

    @GetMapping("/{memberId}")
    public BaseResponse<MemberDetail> getAdminMemberDetail(
        @AuthenticationPrincipal PrincipalDetails user,
        @PathVariable("memberId") Long memberId) {
        return new BaseResponse<MemberDetail>(adminMemberService.getAdminMemberDetail(memberId));
    }

    @GetMapping("/register")
    public BaseResponse<List<RegisterList>> getAdminRegisterList(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<List<RegisterList>>(adminMemberService.getAdminRegisterList());
    }

    @GetMapping("/creator")
    public BaseResponse<List<CreatorList>> getAdminCreatorList(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<List<CreatorList>>(adminMemberService.getAdminCreatorList());
    }

    @PatchMapping("/approve")
    public BaseResponse<String> approveMemberList(@AuthenticationPrincipal PrincipalDetails user,
        @RequestBody AdminMemberDTO.Approve dto) {
        log.info("가입승인 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        log.info("가입승인 API dto - {}", dto);
        return new BaseResponse<String>(adminMemberService.approveUserList(dto));
    }

    @PatchMapping("/{memberId}/creator")
    public BaseResponse<String> improveToCreator(@AuthenticationPrincipal PrincipalDetails user,
        @PathVariable("memberId") Long memberId) {
        log.info("크리에이터 전환 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<String>(adminMemberService.improveToCreator(memberId));
    }

    @DeleteMapping("/{memberId}/creator")
    public BaseResponse<String> rejectCreator(@AuthenticationPrincipal PrincipalDetails user,
        @PathVariable("memberId") Long memberId) {
        log.info("크리에이터 거절 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<String>(adminMemberService.rejectCreator(memberId));
    }

    @DeleteMapping("/{memberId}")
    public BaseResponse<String> deleteMember(@AuthenticationPrincipal PrincipalDetails user,
        @PathVariable("memberId") Long memberId) {
        log.info("회원삭제 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<String>(adminMemberService.deleteMember(memberId));
    }

    @PatchMapping
    public BaseResponse<MemberList> updateMember(@AuthenticationPrincipal PrincipalDetails user,
        @RequestBody AdminMemberDTO.UpdateUser dto) {
        log.info("회원수정 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        log.info("회원수정 API dto - {}", dto);
        return new BaseResponse<MemberList>(adminMemberService.updateMember(dto));
    }
}
