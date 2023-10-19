package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.CareerEntity;
import com.metalancer.backend.users.entity.User;
import java.util.List;

public interface CareerRepository {

    CareerEntity findByIdAndUser(Long careerId, User foundUser);

    List<CareerEntity> findAllByUser(User foundUser);

    void deleteCareer(Long careerId, User user);

    void save(CareerEntity createdCareerEntity);
}
