package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.users.entity.CareerEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CareerRepositoryImpl implements CareerRepository {

    private final CareerJpaRepository careerJpaRepository;

    @Override
    public CareerEntity findByIdAndUser(Long careerId, User foundUser) {
        Optional<CareerEntity> careerEntity = careerJpaRepository.findByIdAndUser(careerId,
            foundUser);
        if (careerEntity.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        return careerEntity.get();
    }

    @Override
    public List<CareerEntity> findAllByUser(User foundUser) {
        return careerJpaRepository.findAllByUserOrderByBeginAtDesc(foundUser);
    }

    @Override
    public void deleteCareer(Long careerId, User user) {
        Optional<CareerEntity> careerEntity = careerJpaRepository.findByIdAndUser(careerId, user);
        careerEntity.ifPresent(careerJpaRepository::delete);
    }

    @Override
    public void save(CareerEntity createdCareerEntity) {
        careerJpaRepository.save(createdCareerEntity);
    }

    @Override
    public Optional<CareerEntity> findOptionalByCreator(CreatorEntity creatorEntity) {
        User user = creatorEntity.getUser();
        return careerJpaRepository.findByUser(user);
    }
}
