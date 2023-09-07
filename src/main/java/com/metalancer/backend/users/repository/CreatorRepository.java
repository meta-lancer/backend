package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.CreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<CreatorEntity, Long> {

}
