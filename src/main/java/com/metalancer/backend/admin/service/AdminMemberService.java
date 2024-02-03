package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.CreatorList;
import com.metalancer.backend.admin.domain.CreatorPendingList;
import com.metalancer.backend.admin.domain.MemberDetail;
import com.metalancer.backend.admin.domain.MemberList;
import com.metalancer.backend.admin.domain.RegisterList;
import com.metalancer.backend.admin.dto.AdminMemberDTO.Approve;
import com.metalancer.backend.admin.dto.AdminMemberDTO.UpdateUser;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminMemberService {

    List<MemberList> getAdminMemberList();

    List<RegisterList> getAdminRegisterList();

    List<CreatorList> getAdminCreatorList();

    String approveUserList(Approve dto);

    MemberList updateMember(UpdateUser dto);

    String deleteMember(Long memberId);

    String improveToCreator(Long memberId);

    MemberDetail getAdminMemberDetail(Long memberId);

    String rejectCreator(Long memberId);

    Page<CreatorPendingList> getAdminCreatorPendingList(Pageable pageable);
}