package com.metalancer.backend.member.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.member.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginTypeAndOauthIdAndStatus(LoginType loginType, String oauthId,
        DataStatus status);

    Optional<User> findByName(String name);
}
