package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.ApproveLink;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApproveLinkRepository extends JpaRepository<ApproveLink, Long> {

    Optional<ApproveLink> findByEmail(String email);

    Optional<ApproveLink> findByEmailAndApprovedAtIsNull(String email);

    Optional<ApproveLink> findByApproveLink(String approveLink);
}
