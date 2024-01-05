package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.users.domain.Portfolio;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.PortfolioEntity;
import com.metalancer.backend.users.entity.PortfolioImagesEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PortfolioRepositoryImpl implements PortfolioRepository {

    private final PortfolioJpaRepository portfolioJpaRepository;
    private final PortfolioImagesJpaRepository portfolioImagesJpaRepository;

    @Override
    public List<Portfolio> findAllByCreator(CreatorEntity creatorEntity) {
        List<PortfolioEntity> portfolioEntities = portfolioJpaRepository.findAllByCreatorEntity(
            creatorEntity);
        List<Portfolio> response = new ArrayList<>();
        for (PortfolioEntity portfolioEntity : portfolioEntities) {
            Portfolio portfolio = portfolioEntity.toDomain();
            List<String> portfolioImages = portfolioImagesJpaRepository.findAllByPortfolioEntityOrderBySeq(
                portfolioEntity).stream().map(PortfolioImagesEntity::getImagePath).toList();
            portfolio.setReferenceFileList(portfolioImages);
            response.add(portfolio);
        }
        return response;
    }

    @Override
    public Optional<PortfolioEntity> findOptionalByIdAndCreator(Long portfolioId,
        CreatorEntity creatorEntity) {
        return portfolioJpaRepository.findByIdAndCreatorEntity(portfolioId, creatorEntity);
    }

    @Override
    public PortfolioEntity findEntityByIdAndCreator(Long portfolioId, CreatorEntity creatorEntity) {
        return portfolioJpaRepository.findByIdAndCreatorEntity(portfolioId, creatorEntity)
            .orElseThrow(
                () -> new NotFoundException("포트폴리오: ", ErrorCode.NOT_FOUND)
            );
    }

    @Override
    public void delete(PortfolioEntity portfolioEntity) {
        portfolioJpaRepository.delete(portfolioEntity);
    }

    @Override
    public void save(PortfolioEntity portfolioEntity) {
        portfolioJpaRepository.save(portfolioEntity);
    }
}
