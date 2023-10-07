package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.dto.MemberList;
import java.util.List;

public interface AdminMemberService {

    List<MemberList> getAdminMemberList();
}