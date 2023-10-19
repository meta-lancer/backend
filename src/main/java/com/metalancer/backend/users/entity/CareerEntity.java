package com.metalancer.backend.users.entity;

import com.metalancer.backend.common.BaseTimeEntity;
import com.metalancer.backend.users.domain.Career;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "career")
@ToString
public class CareerEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748412477241993156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "career_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;
    private String description;
    private LocalDateTime beginAt;
    private LocalDateTime endAt;

    @Builder
    public CareerEntity(User user, String title, String description, LocalDateTime beginAt,
        LocalDateTime endAt) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.beginAt = beginAt;
        this.endAt = endAt;
    }

    public Career toDomain() {
        return Career.builder().careerId(id).title(title)
            .description(description)
            .beginAt(beginAt)
            .endAt(endAt).build();
    }

    public void update(String title, String description, LocalDateTime beginAt,
        LocalDateTime endAt) {
        this.title = title;
        this.description = description;
        this.beginAt = beginAt;
        this.endAt = endAt;
    }

}
