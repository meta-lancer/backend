package com.metalancer.backend.category.entity;

import com.metalancer.backend.admin.dto.AdminCategoryDTO.CategoryList;
import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "genre_galaxy_type")
@ToString
public class GenreGalaxyTypeEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7485122177141999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "genre_galaxy_type_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private TagsEntity tagsEntity;
    private boolean useYn = true;

    @Builder
    public GenreGalaxyTypeEntity(TagsEntity tagsEntity, String name, String nameKor) {
        this.tagsEntity = tagsEntity;
    }

    public MainCategory ToMainCategory() {
        String tagName = tagsEntity != null ? tagsEntity.getTagNameEn() : "all";
        String tagNameKor = tagsEntity != null ? tagsEntity.getTagName() : "전체";
        return MainCategory.builder().name(tagName).nameKor(tagNameKor).build();
    }

    public CategoryList ToAdminCategory() {
        String tagName = tagsEntity != null ? tagsEntity.getTagNameEn() : "all";
        String tagNameKor = tagsEntity != null ? tagsEntity.getTagName() : "전체";
        return CategoryList.builder().categoryId(id).name(tagName).nameKor(tagNameKor).useYn(useYn)
            .build();
    }

    public void toggleUse() {
        this.useYn = !useYn;
    }
}
