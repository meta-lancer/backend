package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.PortfolioImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioReferenceJpaRepository extends
    JpaRepository<PortfolioImagesEntity, Long> {

}
