package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import com.metalancer.backend.category.entity.TagsEntity;
import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagsRepositoryImpl implements TagsRepository {

    private final TagsJpaRepository tagsJpaRepository;

    @Override
    public List<String> findAllByTrendSpotLight(TrendSpotlightTypeEntity trendSpotlightTypeEntity) {
        TagsEntity tagsEntity = tagsJpaRepository.findById(
            trendSpotlightTypeEntity.getTagsEntity().getId()).orElseThrow(
            () -> new NotFoundException("TagsEntity: ", ErrorCode.NOT_FOUND)
        );
        List<TagsEntity> tagsEntityList = tagsJpaRepository.findAllByParentId(
            tagsEntity.getId());
        return tagsEntityList.stream().map(TagsEntity::getTagName).toList();
    }

    @Override
    public List<String> findAllByGenreGalaxy(GenreGalaxyTypeEntity genreGalaxyTypeEntity) {
        TagsEntity tagsEntity = tagsJpaRepository.findById(
            genreGalaxyTypeEntity.getTagsEntity().getId()).orElseThrow(
            () -> new NotFoundException("TagsEntity: ", ErrorCode.NOT_FOUND)
        );
        List<TagsEntity> tagsEntityList = tagsJpaRepository.findAllByParentId(
            tagsEntity.getId());
        return tagsEntityList.stream().map(TagsEntity::getTagName).toList();
    }

    @Override
    public List<String> findAllTrendSpotLightTags() {
        List<Long> trendParentIdList = new ArrayList<>();
        trendParentIdList.add(1L);
        trendParentIdList.add(2L);
        List<TagsEntity> parentTagsEntityList = tagsJpaRepository.findAllByParentIdIsIn(
            trendParentIdList);
        List<Long> parentIdList = parentTagsEntityList.stream().map(TagsEntity::getId)
            .toList();
        return tagsJpaRepository.findAllByParentIdIsIn(parentIdList).stream()
            .map(TagsEntity::getTagName).toList();
    }

    @Override
    public List<String> findAllGenreGalaxyTags() {
        Long genreGalaxyParentId = 4L;
        List<TagsEntity> parentTagsEntityList = tagsJpaRepository.findAllByParentId(
            genreGalaxyParentId);
        List<Long> parentIdList = parentTagsEntityList.stream().map(TagsEntity::getId)
            .toList();
        return tagsJpaRepository.findAllByParentIdIsIn(parentIdList)
            .stream()
            .map(TagsEntity::getTagName).toList();
    }

    @Override
    public List<String> findAllByParentsTagName(String parentsTagName) {
        TagsEntity tagsEntity = tagsJpaRepository.findByTagName(parentsTagName).orElseThrow(
            () -> new NotFoundException("TagsEntity: ", ErrorCode.NOT_FOUND)
        );
        List<TagsEntity> tagsEntityList = tagsJpaRepository.findAllByParentId(
            tagsEntity.getId());
        return tagsEntityList.stream().map(TagsEntity::getTagName).toList();
    }
}