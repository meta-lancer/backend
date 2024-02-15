package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.users.entity.InquiryEntity;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryJpaRepository extends
    JpaRepository<InquiryEntity, Long> {

    Page<InquiryEntity> findAllBy(Pageable pageable);

    Page<InquiryEntity> findAllByUserAndStatus(User user, DataStatus status, Pageable pageable);

    int countAllByReplyIsFalse();

    Optional<InquiryEntity> findByIdAndUserAndStatus(Long id, User user, DataStatus status);

    Optional<InquiryEntity> findByIdAndStatus(Long id, DataStatus status);
}
