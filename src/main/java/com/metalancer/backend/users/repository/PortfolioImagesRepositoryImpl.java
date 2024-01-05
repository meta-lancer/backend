package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.PortfolioEntity;
import com.metalancer.backend.users.entity.PortfolioImagesEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PortfolioImagesRepositoryImpl implements PortfolioImagesRepository {

    private final PortfolioImagesJpaRepository portfolioImagesJpaRepository;

    @Override
    public void saveAll(List<PortfolioImagesEntity> portfolioReferenceEntities) {
        portfolioImagesJpaRepository.saveAll(portfolioReferenceEntities);
    }

    @Override
    public void deleteAllByPortfolioEntity(PortfolioEntity portfolioEntity) {
        List<PortfolioImagesEntity> portfolioImagesEntityList = portfolioImagesJpaRepository.findAllByPortfolioEntityOrderBySeq(
            portfolioEntity);
        portfolioImagesJpaRepository.deleteAll(portfolioImagesEntityList);
    }
}
