package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.PortfolioReferenceEntity;
import java.util.List;

public interface PortfolioReferenceRepository {


    void saveAll(List<PortfolioReferenceEntity> portfolioReferenceEntities);
}
