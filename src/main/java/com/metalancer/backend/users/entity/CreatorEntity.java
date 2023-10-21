package com.metalancer.backend.users.entity;

import com.metalancer.backend.admin.domain.CreatorList;
import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.common.exception.InvalidRoleException;
import com.metalancer.backend.users.domain.Creator;
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
@Entity(name = "creators")
@ToString
public class CreatorEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 741101677241993156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "creator_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String location;
    private String address;
    private String company;
    private String email;
    private String introduction;
    private String introductionShort;

    @Builder
    public CreatorEntity(User user, String location, String address, String company, String email,
        String introduction, String introductionShort) {
        checkIfUserHasSellerAuthority(user);
        this.user = user;
        this.location = location;
        this.address = address;
        this.company = company;
        this.email = email;
        this.introduction = introduction;
        this.introductionShort = introductionShort;
    }

    private void checkIfUserHasSellerAuthority(User user) {
        if (!(user.getRole().equals(Role.ROLE_SELLER) || user.getRole().equals(Role.ROLE_ADMIN))) {
            throw new InvalidRoleException(ErrorCode.INVALID_ROLE_ACCESS);
        }
    }

    public Creator toDomain() {
        return Creator.builder().creatorId(id).nickName(user.getName()).profileImg("").email(email)
            .introduction(introduction).introductionShort(introductionShort).satisficationRate(0.0)
            .userType("일반회원").cnt(0).build();
    }

    public CreatorList toAdminCreatorList() {
        return CreatorList.builder()
            .memberId(user.getId())
            .creatorId(id)
            .email(user.getEmail())
            .mobile(user.getMobile())
            .name(user.getName())
            .username(user.getUsername())
            .job(user.getJob())
            .loginType(user.getLoginType())
            .role(user.getRole())
            .status(getStatus())
            .createdAt(getCreatedAt())
            .updatedAt(getUpdatedAt())
            .build();
    }
}
