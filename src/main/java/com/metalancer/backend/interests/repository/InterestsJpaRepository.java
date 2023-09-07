package com.metalancer.backend.interests.repository;

import com.metalancer.backend.interests.entity.InterestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestsJpaRepository extends JpaRepository<InterestsEntity, Long> {

}
