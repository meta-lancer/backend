package com.metalancer.backend.user.repository;

import com.metalancer.backend.user.domain.Email;
import com.metalancer.backend.user.entity.ApproveLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApproveLinkRepository extends JpaRepository<ApproveLink, Long> {
    Optional<ApproveLink> findByEmail(Email email);

    Optional<ApproveLink> findByApproveLink(String approveLink);
}
