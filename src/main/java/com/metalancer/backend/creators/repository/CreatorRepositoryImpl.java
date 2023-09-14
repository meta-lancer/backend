package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
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
}
