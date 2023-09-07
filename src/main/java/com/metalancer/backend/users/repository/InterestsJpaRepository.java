package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.InterestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestsJpaRepository extends JpaRepository<InterestsEntity, Long> {

}
