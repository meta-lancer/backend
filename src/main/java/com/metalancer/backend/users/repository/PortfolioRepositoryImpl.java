package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.users.domain.Portfolio;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.PortfolioEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PortfolioRepositoryImpl implements PortfolioRepository {

    private final PortfolioJpaRepository portfolioJpaRepository;

    @Override
    public List<Portfolio> findAllByCreator(CreatorEntity creatorEntity) {
        return portfolioJpaRepository.findAllByCreatorEntity(creatorEntity).stream().map(
            PortfolioEntity::toDomain).collect(Collectors.toList());
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
