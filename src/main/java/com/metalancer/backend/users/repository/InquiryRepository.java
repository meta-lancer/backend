package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.InquiryEntity;
import java.util.Optional;

public interface InquiryRepository {

    void save(InquiryEntity inquiryEntity);

    Optional<InquiryEntity> findById(Long id);
}
