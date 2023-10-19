package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.interests.domain.Interests;
import com.metalancer.backend.users.entity.User;
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

    @Override
    public List<UserInterestsEntity> findAllByUser(User user) {
        return userInterestsJpaRepository.findAllByUserAndStatus(user, DataStatus.ACTIVE);
    }

    @Override
    public List<Interests> findAllDomainByUser(User user) {
        List<UserInterestsEntity> userInterestsEntities = userInterestsJpaRepository.findAllByUserAndStatus(
            user, DataStatus.ACTIVE);
        return userInterestsEntities.stream()
            .map(UserInterestsEntity::toDomain).toList();
    }

    @Override
    public void deleteAll(List<UserInterestsEntity> userInterestsEntities) {
        userInterestsJpaRepository.deleteAll(userInterestsEntities);
    }

    @Override
    public void deleteAllByUser(User foundUser) {
        List<UserInterestsEntity> userInterestsEntityList = findAllByUser(
            foundUser);
        userInterestsJpaRepository.deleteAll(userInterestsEntityList);
    }
}
