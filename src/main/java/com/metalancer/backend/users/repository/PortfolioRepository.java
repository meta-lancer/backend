package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.domain.Portfolio;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.List;

public interface PortfolioRepository {

    List<Portfolio> findAllByCreator(CreatorEntity creatorEntity);
}
