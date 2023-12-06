package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.domain.Portfolio;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.PortfolioEntity;
import java.util.List;
import java.util.Optional;

public interface PortfolioRepository {

    List<Portfolio> findAllByCreator(CreatorEntity creatorEntity);

    Optional<PortfolioEntity> findOptionalByIdAndCreator(Long portfolioId,
        CreatorEntity creatorEntity);

    PortfolioEntity findEntityByIdAndCreator(Long portfolioId, CreatorEntity creatorEntity);

    void delete(PortfolioEntity portfolioEntity);

    void save(PortfolioEntity portfolioEntity);
}
