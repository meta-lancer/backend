package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginTypeAndOauthIdAndStatus(LoginType loginType, String oauthId,
        DataStatus status);

    Optional<User> findByLoginTypeAndOauthId(LoginType loginType, String oauthId);

    Optional<User> findByEmail(String email);

    int countUsersByNickname(String nickname);
}
