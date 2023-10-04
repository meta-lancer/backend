package com.metalancer.backend.category.entity;

import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "trend_spotlight_type")
@ToString
public class TrendSpotlightTypeEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 745122177141999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "trend_spotlight_type_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String nameKor;

    @Builder
    public TrendSpotlightTypeEntity(String name, String nameKor) {
        this.name = name;
        this.nameKor = nameKor;
    }

    public MainCategory ToMainCategory() {
        return MainCategory.builder().name(name).nameKor(nameKor).build();
    }
}
