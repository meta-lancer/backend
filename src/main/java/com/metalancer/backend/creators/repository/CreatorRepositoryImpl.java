package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreatorRepositoryImpl implements CreatorRepository {

    private final CreatorJpaRepository creatorJpaRepository;

    @Override
    public CreatorEntity findByUserAndStatus(User user, DataStatus status) {
        return creatorJpaRepository.findByUserAndStatus(user, status).orElseThrow(
            () -> new BaseException(ErrorCode.NOT_FOUND)
        );
    }

    @Override
    public Optional<CreatorEntity> findOptionalByUserAndStatus(User user, DataStatus status) {
        return creatorJpaRepository.findByUserAndStatus(user, status);
    }

    @Override
    public CreatorEntity findByCreatorId(Long creatorId) {
        return creatorJpaRepository.findById(creatorId).orElseThrow(
            () -> new BaseException(ErrorCode.NOT_FOUND)
        );
    }

    @Override
    public List<CreatorEntity> findAll() {
        return creatorJpaRepository.findAll();
    }

    @Override
    public void save(CreatorEntity createdCreator) {
        creatorJpaRepository.save(createdCreator);
    }
}
