package com.metalancer.backend.users.entity;

import com.metalancer.backend.common.BaseTimeEntity;
import com.metalancer.backend.users.domain.Portfolio;
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
@Entity(name = "portfolio")
@ToString
public class PortfolioEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7484153257241993156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "portfolio_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creatorEntity;

    private String title;
    private LocalDateTime beginAt;
    private LocalDateTime endAt;
    private int workerCnt;
    private String tool;
    private String referenceFile;

    @Builder
    public PortfolioEntity(CreatorEntity creatorEntity, String title, LocalDateTime beginAt,
        LocalDateTime endAt, int workerCnt, String tool, String referenceFile) {
        this.creatorEntity = creatorEntity;
        this.title = title;
        this.beginAt = beginAt;
        this.endAt = endAt;
        this.workerCnt = workerCnt;
        this.tool = tool;
        this.referenceFile = referenceFile;
    }

    public Portfolio toDomain() {
        return Portfolio.builder().portfolioId(id).title(title)
            .beginAt(beginAt).endAt(endAt).workerCnt(workerCnt).tool(tool)
            .referenceFile(referenceFile).build();
    }

    public void Update(String title, LocalDateTime beginAt,
        LocalDateTime endAt, int workerCnt, String tool, String referenceFile) {
        this.title = title;
        this.beginAt = beginAt;
        this.endAt = endAt;
        this.workerCnt = workerCnt;
        this.tool = tool;
        this.referenceFile = referenceFile;
    }
}
