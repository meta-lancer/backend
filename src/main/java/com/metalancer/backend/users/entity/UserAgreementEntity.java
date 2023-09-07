package com.metalancer.backend.users.entity;

import com.metalancer.backend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user_agreement")
@ToString
public class UserAgreementEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 754409188241993156L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_agreement_id", nullable = false)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private boolean ageAgree;
    @Column(nullable = false)
    private boolean serviceAgree;
    @Column(nullable = false)
    private boolean infoAgree;
    @Column(nullable = false)
    private boolean marketingAgree;
    @Column(nullable = false)
    private boolean statusAgree;

    @Builder
    public UserAgreementEntity(User user, boolean ageAgree, boolean serviceAgree, boolean infoAgree,
        boolean marketingAgree, boolean statusAgree) {
        this.user = user;
        this.ageAgree = ageAgree;
        this.serviceAgree = serviceAgree;
        this.infoAgree = infoAgree;
        this.marketingAgree = marketingAgree;
        this.statusAgree = statusAgree;
    }
}

