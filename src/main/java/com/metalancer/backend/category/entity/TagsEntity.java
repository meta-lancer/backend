package com.metalancer.backend.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "tags")
@ToString
public class TagsEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 741155177141999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "tag_id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private int depth;
    @Column(nullable = false)
    private String tagName;
    @Column(nullable = false)
    private String tagNameEn;
    @Column(name = "order", nullable = false)
    private int ord;
    private Long parentId;
}
