package com.metalancer.backend.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.member.domain.Email;
import com.metalancer.backend.member.domain.Mobile;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "MEMBER_NUMBER", nullable = false)
    private Integer id;

    @Embedded
    private Email email;

    private String name;

    @Embedded
    private Mobile mobile;

    @JsonIgnore
    private String password;

    @Enumerated
    LoginType loginType;

    public Member(String email, String name, String mobile, String password) {
        this.email = new Email(email);
        this.name = name;
        this.mobile = new Mobile(mobile);
        this.password = password;
    }

    public void withdraw() {
    }

    public void update() {

    }
}
