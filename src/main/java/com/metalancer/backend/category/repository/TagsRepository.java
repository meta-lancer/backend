package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import com.metalancer.backend.category.entity.TagsEntity;
import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import java.util.List;

public interface TagsRepository {

    List<String> findAllByTrendSpotLight(TrendSpotlightTypeEntity trendSpotlightTypeEntity);

    List<String> findAllByGenreGalaxy(GenreGalaxyTypeEntity genreGalaxyTypeEntity);

    List<String> findAllTrendSpotLightTags();

    List<String> findAllGenreGalaxyTags();

    List<String> findAllByParentsTagName(String parentsTagName);

    List<String> findAllByKeywordLimit10(String keyword);

    List<TagsEntity> findAllMainCategoryByParentsTagName(String parentsTagName);

    String findStringByTagName(String tagName);

    List<TagsEntity> findAllParentsTagName(int depth);

    List<TagsEntity> findAllParentsTagAndChildTagsByParentTag(TagsEntity parentTag);

    boolean productsHasAnyTagInTagList(ProductsEntity productsEntity,
        List<TagsEntity> parentTagAndChildTagList);
}
