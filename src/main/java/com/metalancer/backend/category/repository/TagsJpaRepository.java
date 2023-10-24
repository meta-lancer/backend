package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.TagsEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsJpaRepository extends JpaRepository<TagsEntity, Long> {

    List<TagsEntity> findAllByParentId(Long parentId);

    List<TagsEntity> findAllByParentIdIsIn(List<Long> parentId);
}
