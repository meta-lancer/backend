package com.metalancer.backend.users.repository;

import com.metalancer.backend.admin.domain.InquiryList;
import com.metalancer.backend.users.entity.InquiryEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InquiryRepository {

    void save(InquiryEntity inquiryEntity);

    Optional<InquiryEntity> findById(Long id);

    Page<InquiryList> findAdminAll(Pageable pageable);

    Integer countNewCnt();
}
