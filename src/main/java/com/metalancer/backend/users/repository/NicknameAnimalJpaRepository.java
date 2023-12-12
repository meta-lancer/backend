package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.NicknameAnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NicknameAnimalJpaRepository extends JpaRepository<NicknameAnimalEntity, Long> {

}
