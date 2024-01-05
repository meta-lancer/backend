package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CreatorJpaRepository extends JpaRepository<CreatorEntity, Long> {

    Optional<CreatorEntity> findByUserAndStatus(User user, DataStatus status);

    Optional<CreatorEntity> findByUser(User user);

    @Query("select count(c) from creators c where c.createdAt between :startDate and :startOfNextDay")
    Integer getRegisterCntByDate(@Param("startDate") LocalDateTime startDate,
        @Param("startOfNextDay") LocalDateTime startOfNextDay);
}
