package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.CareerEntity;
import com.metalancer.backend.users.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CareerRepositoryImpl implements CareerRepository {

    private final CareerJpaRepository careerJpaRepository;

    @Override
    public List<CareerEntity> findAllByUser(User foundUser) {
        return careerJpaRepository.findAllByUserOrderByBeginAtDesc(foundUser);
    }
}
