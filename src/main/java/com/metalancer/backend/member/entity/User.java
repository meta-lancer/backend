package com.metalancer.backend.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.member.domain.Email;
import com.metalancer.backend.member.domain.Mobile;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "member")
@ToString
public class User extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(nullable = false)
    private Long memberId;
    private String oauthId;

    @Embedded
    private Email email;

    private String name;
    private String username;

    @Embedded
    private Mobile mobile;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Builder
    public User(String email, String oauthId, String mobile, String password,
        LoginType loginType, String name, String username) {
        this.email = new Email(email);
        this.oauthId = oauthId;
        this.mobile = new Mobile(mobile);
        this.password = password;
        this.loginType = loginType;
        this.name = name;
        this.username = username;
    }

    public void withdraw() {
    }

    public void update() {

    }

}
