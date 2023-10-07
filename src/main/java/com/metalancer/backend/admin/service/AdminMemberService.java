package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.dto.CreatorList;
import com.metalancer.backend.admin.dto.MemberList;
import com.metalancer.backend.admin.dto.RegisterList;
import java.util.List;

public interface AdminMemberService {

    List<MemberList> getAdminMemberList();

    List<RegisterList> getAdminRegisterList();

    List<CreatorList> getAdminCreatorList();
}