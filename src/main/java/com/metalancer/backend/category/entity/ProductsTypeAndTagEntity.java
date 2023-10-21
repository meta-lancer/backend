package com.metalancer.backend.category.entity;

import com.metalancer.backend.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "products_type_and_tag")
@ToString
public class ProductsTypeAndTagEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 76359177241999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "products_type_and_tag_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trend_spotlight_type_id")
    private TrendSpotlightTypeEntity trendSpotlightTypeEntity;

    @ManyToOne
    @JoinColumn(name = "genre_galaxy_type_id")
    private GenreGalaxyTypeEntity genreGalaxyTypeEntity;

    @Column(nullable = false)
    private String productsTagName;
}
