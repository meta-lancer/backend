package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.InquiryList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminInquiryService {

    Page<InquiryList> getAdminInquiryList(Pageable pageable);

    Integer getAdminInquiryNewCount();
}