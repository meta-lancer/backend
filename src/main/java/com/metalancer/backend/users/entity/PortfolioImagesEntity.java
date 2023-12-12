package com.metalancer.backend.users.entity;

import com.metalancer.backend.common.BaseTimeEntity;
import com.metalancer.backend.users.domain.PortfolioReference;
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
@Entity(name = "portfolio_images")
@ToString
public class PortfolioImagesEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748415572434233156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "portfolio_image_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private PortfolioEntity portfolioEntity;

    private String imagePath;
    private int seq;

    @Builder
    public PortfolioImagesEntity(PortfolioEntity portfolioEntity, String imagePath, int seq) {
        this.portfolioEntity = portfolioEntity;
        this.imagePath = imagePath;
        this.seq = seq;
    }

    public PortfolioReference toDomain() {
        return PortfolioReference.builder().url(imagePath).ord(seq).build();
    }

}
