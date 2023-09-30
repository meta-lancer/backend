package com.metalancer.backend.category.entity;

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
@Entity(name = "hot_pick")
@ToString
public class HotPickTypeEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7410122177141999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "hot_pick_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Builder
    public HotPickTypeEntity(String name) {
        this.name = name;
    }
}
