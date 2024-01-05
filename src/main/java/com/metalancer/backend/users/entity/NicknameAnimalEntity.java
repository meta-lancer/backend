package com.metalancer.backend.users.entity;

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
@Entity(name = "nickname_animal")
@ToString
public class NicknameAnimalEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748413277241993156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "nickname_animal_id", nullable = false)
    private Long id;

    @Column(nullable = true)
    private String animal;
}
