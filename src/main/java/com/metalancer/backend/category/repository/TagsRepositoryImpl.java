package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import com.metalancer.backend.category.entity.TagsEntity;
import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsTagEntity;
import com.metalancer.backend.products.repository.ProductsTagJpaRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagsRepositoryImpl implements TagsRepository {

    private final TagsJpaRepository tagsJpaRepository;
    private final ProductsTagJpaRepository productsTagJpaRepository;
    private final GenreGalaxyTypeJpaRepository genreGalaxyTypeJpaRepository;

    @Override
    public List<String> findAllByTrendSpotLight(TrendSpotlightTypeEntity trendSpotlightTypeEntity) {
        TagsEntity tagsEntity = tagsJpaRepository.findById(
            trendSpotlightTypeEntity.getTagsEntity().getId()).orElseThrow(
            () -> new NotFoundException("TagsEntity: ", ErrorCode.NOT_FOUND)
        );
        // ex) 마인크래프트에 종속된
        List<TagsEntity> tagsEntityList = tagsJpaRepository.findAllByParentId(
            tagsEntity.getId());
        // 마인크래프트는 앞에 추가
        tagsEntityList.add(0, tagsEntity);
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
        tagsEntityList.add(0, tagsEntity);
        return tagsEntityList.stream().map(TagsEntity::getTagName).toList();
    }

    @Override
    public List<String> findAllTrendSpotLightTags() {
        List<Long> trendParentIdList = new ArrayList<>();
        trendParentIdList.add(1L);
        trendParentIdList.add(2L);
        List<TagsEntity> parentTagsEntityList = tagsJpaRepository.findAllByParentIdIsIn(
            trendParentIdList);
        List<Long> trendIdList = parentTagsEntityList.stream().map(TagsEntity::getId)
            .toList();
        return tagsJpaRepository.findAllByIdIsIn(trendIdList).stream()
            .map(TagsEntity::getTagName).toList();
    }

    @Override
    public List<String> findAllGenreGalaxyTags() {
        // 모든 genreGalaxy에 해당되는 tag 불러오기
        List<TagsEntity> genreGalaxyTagList = genreGalaxyTypeJpaRepository.findAllGenreGalaxyTagList();

//        Long genreGalaxyParentId = 4L;
//        List<TagsEntity> parentTagsEntityList = tagsJpaRepository.findAllByParentId(
//            genreGalaxyParentId);
//        List<Long> parentIdList = parentTagsEntityList.stream().map(TagsEntity::getId)
//            .toList();
        List<TagsEntity> genreGalaxyTagAndDescentList = new ArrayList<>();
        // 돌면서
        for (TagsEntity genreTag : genreGalaxyTagList) {
            // 자기가 부모인 경우 자식 태그들 싹 다 불러오기
            List<TagsEntity> tagsEntityList = tagsJpaRepository.findAllByParentId(
                genreTag.getId());
            // 자기 자신도 태그에 포함
            tagsEntityList.add(0, genreTag);
            genreGalaxyTagAndDescentList.addAll(tagsEntityList);
        }
//        return tagsJpaRepository.findAllByParentIdIsIn(parentIdList)
//            .stream()
//            .map(TagsEntity::getTagName).toList();
        return genreGalaxyTagAndDescentList
            .stream()
            .map(TagsEntity::getTagName).toList();
    }

    @Override
    public List<String> findAllByParentsTagName(String parentsTagName) {
        TagsEntity tagsEntity = tagsJpaRepository.findByTagNameOrTagNameEn(parentsTagName)
            .orElseThrow(
                () -> new NotFoundException("TagsEntity: ", ErrorCode.NOT_FOUND)
            );
        List<TagsEntity> tagsEntityList = tagsJpaRepository.findAllByParentId(
            tagsEntity.getId());
        return tagsEntityList.stream().map(TagsEntity::getTagName).toList();
    }

    @Override
    public String findStringByTagName(String tagName) {
        TagsEntity tagsEntity = tagsJpaRepository.findByTagNameOrTagNameEn(tagName)
            .orElseThrow(
                () -> new NotFoundException("TagsEntity: ", ErrorCode.NOT_FOUND)
            );
        return tagsEntity.getTagName();
    }

    @Override
    public List<TagsEntity> findAllParentsTagName(int depth) {
        return tagsJpaRepository.findAllByDepth(depth);
    }

    @Override
    public List<TagsEntity> findAllParentsTagAndChildTagsByParentTag(TagsEntity parentTag) {
        Long parentId = parentTag.getId();
        List<TagsEntity> response = tagsJpaRepository.findAllByParentId(parentId);
        response.add(parentTag);
        return response;
    }

    @Override
    public boolean productsHasAnyTagInTagList(ProductsEntity productsEntity,
        List<TagsEntity> parentTagAndChildTagList) {
        List<String> tagsNameProductsHas = productsTagJpaRepository.findAllByProductsEntityOrderByNameAsc(
            productsEntity).stream().map(
            ProductsTagEntity::getName).toList();
        List<TagsEntity> tagsEntityProductsHas = new ArrayList<>();
        for (String tagsName : tagsNameProductsHas) {
            // 영문이나 한글 태그명
            Optional<TagsEntity> tagsEntityOptional = tagsJpaRepository.findByTagNameOrTagNameEn(
                tagsName);
            tagsEntityOptional.ifPresent(tagsEntityProductsHas::add);
        }
        // 하나라도 포함되면 true
        return tagsEntityProductsHas.stream()
            .anyMatch(parentTagAndChildTagList::contains);
    }

    @Override
    public List<String> findAllByKeywordLimit10(String keyword) {
        List<TagsEntity> tagsList = tagsJpaRepository.findAllByTagNameContains("%" + keyword + "%");
        return tagsList.stream().map(TagsEntity::getTagName).sorted(
                Comparator.comparingInt((String name) -> name.indexOf(keyword))
                    .thenComparing(name -> name))
            .limit(10).toList();
    }

    @Override
    public List<TagsEntity> findAllMainCategoryByParentsTagName(String parentsTagName) {
        TagsEntity tagsEntity = tagsJpaRepository.findByTagName(parentsTagName).orElseThrow(
            () -> new NotFoundException("TagsEntity: ", ErrorCode.NOT_FOUND)
        );
        return tagsJpaRepository.findAllByParentId(tagsEntity.getId());
    }
}
