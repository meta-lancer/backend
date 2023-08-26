package com.metalancer.backend.users.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.common.exception.InvalidRoleException;
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
public class Creator extends BaseEntity implements Serializable {

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
    public Creator(User user, String location, String address, String company, String email,
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
}
