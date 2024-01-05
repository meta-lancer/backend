package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.InquiryList;
import com.metalancer.backend.admin.dto.AdminMemberDTO.CreateUpdateReply;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminInquiryService {

    Page<InquiryList> getAdminInquiryList(Pageable pageable);

    Integer getAdminInquiryNewCount();

    Boolean replyInquiry(PrincipalDetails user, Long inquiryId, CreateUpdateReply dto);
}