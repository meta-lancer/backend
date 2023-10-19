package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.CareerEntity;
import com.metalancer.backend.users.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerJpaRepository extends JpaRepository<CareerEntity, Long> {

    List<CareerEntity> findAllByUserOrderByBeginAtDesc(User user);
}
