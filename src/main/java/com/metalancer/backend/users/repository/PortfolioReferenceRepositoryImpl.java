package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.PortfolioReferenceEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PortfolioReferenceRepositoryImpl implements PortfolioReferenceRepository {

    private final PortfolioReferenceJpaRepository portfolioReferenceJpaRepository;

    @Override
    public void saveAll(List<PortfolioReferenceEntity> portfolioReferenceEntities) {
        portfolioReferenceJpaRepository.saveAll(portfolioReferenceEntities);
    }
}
