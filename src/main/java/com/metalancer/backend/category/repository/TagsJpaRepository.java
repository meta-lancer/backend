package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.TagsEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagsJpaRepository extends JpaRepository<TagsEntity, Long> {

    List<TagsEntity> findAllByParentId(Long parentId);

    List<TagsEntity> findAllByParentIdIsIn(List<Long> parentId);

    List<TagsEntity> findAllByIdIsIn(List<Long> id);

    Optional<TagsEntity> findByTagName(String tagName);

    @Query("select t from tags t where t.tagName = :tagName or t.tagNameEn = :tagName")
    Optional<TagsEntity> findByTagNameOrTagNameEn(@Param("tagName") String tagName);

    @Query("select t from tags t where t.depth = 3 and t.tagName like :tagName")
    List<TagsEntity> findAllByTagNameContains(@Param("tagName") String tagName);

    List<TagsEntity> findAllByDepth(int depth);
}
