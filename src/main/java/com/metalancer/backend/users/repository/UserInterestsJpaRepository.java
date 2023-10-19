package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.entity.UserInterestsEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestsJpaRepository extends JpaRepository<UserInterestsEntity, Long> {

    List<UserInterestsEntity> findAllByUserAndStatus(User user, DataStatus status);
}
