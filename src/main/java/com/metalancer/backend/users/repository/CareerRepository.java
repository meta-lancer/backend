package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.CareerEntity;
import com.metalancer.backend.users.entity.User;
import java.util.List;

public interface CareerRepository {

    List<CareerEntity> findAllByUser(User foundUser);
}
