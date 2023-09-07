package com.metalancer.backend.interests.entity;

import com.metalancer.backend.common.BaseTimeEntity;
import com.metalancer.backend.interests.domain.Interests;
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
@Entity(name = "interests")
@ToString
public class InterestsEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748309188241993156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "interests_id", nullable = false)
    private Long id;
    private String name;
    private int ord;

    @Builder
    public InterestsEntity(String name, int ord) {
        this.name = name;
        this.ord = ord;
    }

    public Interests toDomain() {
        return Interests.builder().interestsName(name).ord(ord).build();
    }
}
