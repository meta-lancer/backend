package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.UserAgreementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAgreementJpaRepository extends JpaRepository<UserAgreementEntity, Long> {

}
