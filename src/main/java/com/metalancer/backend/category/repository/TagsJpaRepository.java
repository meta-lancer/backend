package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.TagsEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsJpaRepository extends JpaRepository<TagsEntity, Long> {

    List<TagsEntity> findAllByParentId(Long parentId);

    List<TagsEntity> findAllByParentIdIsIn(List<Long> parentId);

    Optional<TagsEntity> findByTagName(String tagName);
}
