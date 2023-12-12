package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.PortfolioEntity;
import com.metalancer.backend.users.entity.PortfolioImagesEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioImagesJpaRepository extends
    JpaRepository<PortfolioImagesEntity, Long> {

    List<PortfolioImagesEntity> findAllByPortfolioEntityOrderBySeq(PortfolioEntity portfolioEntity);
}
