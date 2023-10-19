package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.domain.Portfolio;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.PortfolioEntity;
import java.util.List;
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
}
