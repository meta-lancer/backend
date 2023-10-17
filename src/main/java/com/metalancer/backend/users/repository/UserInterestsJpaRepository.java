package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.UserInterestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestsJpaRepository extends JpaRepository<UserInterestsEntity, Long> {

}
