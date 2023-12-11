package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryJpaRepository extends
    JpaRepository<InquiryEntity, Long> {

}
