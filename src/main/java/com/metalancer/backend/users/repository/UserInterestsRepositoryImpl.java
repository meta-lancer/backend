package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.UserInterestsEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserInterestsRepositoryImpl implements UserInterestsRepository {

    private final UserInterestsJpaRepository userInterestsJpaRepository;

    @Override
    public void saveAll(List<UserInterestsEntity> userInterestsEntities) {
        userInterestsJpaRepository.saveAll(userInterestsEntities);
    }
}
