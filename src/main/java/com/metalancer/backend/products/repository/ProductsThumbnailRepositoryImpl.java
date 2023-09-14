package com.metalancer.backend.products.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsThumbnailRepositoryImpl implements ProductsThumbnailRepository {

    private final ProductsThumbnailJpaRepository productsThumbnailJpaRepository;

}
