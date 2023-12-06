package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.users.entity.ApproveLink;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApproveLinkRepository extends JpaRepository<ApproveLink, Long> {

    Optional<ApproveLink> findByEmail(String email);

    Optional<ApproveLink> findByEmailAndApprovedAtIsNull(String email);

    Optional<ApproveLink> findByApproveLink(String approveLink);

    Optional<ApproveLink> findByUserAndStatus(User user, DataStatus status);
}
