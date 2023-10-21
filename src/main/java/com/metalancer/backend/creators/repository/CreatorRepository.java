package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;

public interface CreatorRepository {

    CreatorEntity findByUserAndStatus(User user, DataStatus dataStatus);

    Optional<CreatorEntity> findOptionalByUserAndStatus(User user, DataStatus dataStatus);

    CreatorEntity findByCreatorId(Long creatorId);
}
