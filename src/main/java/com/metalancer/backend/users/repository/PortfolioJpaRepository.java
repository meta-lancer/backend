package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.PortfolioEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioJpaRepository extends JpaRepository<PortfolioEntity, Long> {

    List<PortfolioEntity> findAllByCreatorEntity(CreatorEntity creatorEntity);

    Optional<PortfolioEntity> findByIdAndCreatorEntity(Long id, CreatorEntity creatorEntity);
}
