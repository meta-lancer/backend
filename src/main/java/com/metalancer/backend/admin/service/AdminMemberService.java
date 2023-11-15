package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.CreatorList;
import com.metalancer.backend.admin.domain.MemberList;
import com.metalancer.backend.admin.domain.RegisterList;
import com.metalancer.backend.admin.dto.AdminMemberDTO.Approve;
import com.metalancer.backend.admin.dto.AdminMemberDTO.UpdateUser;
import com.metalancer.backend.users.entity.User;
import java.util.List;

public interface AdminMemberService {

    List<MemberList> getAdminMemberList();

    List<RegisterList> getAdminRegisterList();

    List<CreatorList> getAdminCreatorList();

    String approveUserList(Approve dto);

    MemberList updateMember(UpdateUser dto);

    String deleteMember(Long memberId);

    String improveToCreator(Long memberId);

    User getAdminMemberDetail(Long memberId);
}