package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.InquiryList;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.users.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AdminInquiryServiceImpl implements AdminInquiryService {

    private final InquiryRepository inquiryRepository;

    @Override
    public Page<InquiryList> getAdminInquiryList(Pageable pageable) {
        return inquiryRepository.findAdminAll(pageable);
    }
}