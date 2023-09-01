package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.TagEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    @Override
    public List<String> findTagListByProduct(ProductsEntity products) {
        return tagJpaRepository.findAllByProductsEntityOrderByNameAsc(products).stream().map(
            TagEntity::getName).collect(Collectors.toList());
    }
}
