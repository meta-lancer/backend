package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;

public interface CreatorRepository {

    CreatorEntity findByUserAndStatus(User user, DataStatus dataStatus);

    CreatorEntity findByCreatorId(Long creatorId);
}
