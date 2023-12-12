package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.PortfolioImagesEntity;
import java.util.List;

public interface PortfolioImagesRepository {


    void saveAll(List<PortfolioImagesEntity> portfolioReferenceEntities);
}
