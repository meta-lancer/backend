package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.UserAgreementEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserAgreementRepositoryImpl implements UserAgreementRepository {

    private final UserAgreementJpaRepository userAgreementJpaRepository;

    @Override
    public void save(UserAgreementEntity savedUserAgreementEntity) {
        userAgreementJpaRepository.save(savedUserAgreementEntity);
    }
}
