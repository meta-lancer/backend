package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import java.util.List;

public interface GenreGalaxyTypeRepository {

    List<MainCategory> getGenreGalaxyCategoryList();

    List<MainCategory> getGenreGalaxyCategoryListWithUseYnTrue();

    GenreGalaxyTypeEntity findByName(String type);

    List<MainCategory> getGenreGalaxyCategoryListWithOutAll();
}
