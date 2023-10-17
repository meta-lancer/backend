package com.metalancer.backend.interests.repository;

import com.metalancer.backend.interests.domain.Interests;
import com.metalancer.backend.interests.entity.InterestsEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InterestsRepositoryImpl implements InterestsRepository {

    private final InterestsJpaRepository interestsJpaRepository;

    @Override
    public List<Interests> findAll() {
        return interestsJpaRepository.findAll().stream().map(InterestsEntity::toDomain)
            .collect(Collectors.toList());
    }
}
