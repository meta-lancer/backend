package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.UserInterestsEntity;
import java.util.List;

public interface UserInterestsRepository {

    void saveAll(List<UserInterestsEntity> userInterestsEntities);
}