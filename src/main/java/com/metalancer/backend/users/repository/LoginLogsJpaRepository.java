package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.LoginLogsEntity;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoginLogsJpaRepository extends JpaRepository<LoginLogsEntity, Long> {

    @Query("select count(DISTINCT ll.user) from login_logs ll where ll.loginAt between :startDate and :startOfNextDay")
    Integer getLoginCntDaily(@Param("startDate") LocalDateTime startDate,
        @Param("startOfNextDay") LocalDateTime startOfNextDay);

    @Query("select count(DISTINCT (ll.user, DATE_FORMAT(ll.loginAt, '%Y-%m-%d'))) from login_logs ll where ll.loginAt between :startDate and :startOfNextDay")
    Integer getLoginCntYearly(@Param("startDate") LocalDateTime startDate,
        @Param("startOfNextDay") LocalDateTime startOfNextDay);
}
