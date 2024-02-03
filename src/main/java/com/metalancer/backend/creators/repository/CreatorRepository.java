package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CreatorRepository {

    CreatorEntity findByUserAndStatus(User user, DataStatus dataStatus);

    Optional<CreatorEntity> findOptionalByUserAndStatus(User user, DataStatus dataStatus);

    Optional<CreatorEntity> findOptionalByUser(User user);


    CreatorEntity findByCreatorId(Long creatorId);

    List<CreatorEntity> findAll();

    void save(CreatorEntity createdCreator);

    Integer getRegisterCntByDate(LocalDateTime date, LocalDateTime startOfNextDay);

    void delete(CreatorEntity foundPendingCreator);
    
}
