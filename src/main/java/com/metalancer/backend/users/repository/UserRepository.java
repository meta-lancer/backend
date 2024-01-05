package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.users.entity.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginTypeAndOauthIdAndStatus(LoginType loginType, String oauthId,
        DataStatus status);

    Optional<User> findByLoginTypeAndOauthId(LoginType loginType, String oauthId);

    Optional<User> findByEmail(String email);

    int countUsersByNickname(String nickname);

    @Query("select count(u) from users u where u.createdAt between :startDate and :startOfNextDay")
    Integer getRegisterCntByDate(@Param("startDate") LocalDateTime startDate,
        @Param("startOfNextDay") LocalDateTime startOfNextDay);


    Integer countUserBy();

    Integer countAllByStatus(DataStatus status);

    Integer countAllByLoginType(LoginType loginType);

    @Query("select count(u) from users u left join creators c on u = c.user where c.id is null")
    Integer getNormalUserCnt();

    @Query("select count(u) from users u left join creators c on u = c.user where c.id is not null")
    Integer getCreatorUserCnt();

}
