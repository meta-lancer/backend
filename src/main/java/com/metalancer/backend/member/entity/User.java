package com.metalancer.backend.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.StatusException;
import com.metalancer.backend.member.domain.Email;
import com.metalancer.backend.member.domain.Mobile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.UUID;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "member")
@ToString
public class User extends BaseEntity implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

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

    public void setNormalUsername() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(loginType.getProvider()).append("_").append(UUID.randomUUID().toString(), 0, 10);
        this.username = stringBuilder.toString();
    }

    public void setPending() {
        pend();
    }

    public void withdraw() {
        delete();
    }

    public void update() {

    }

    public DataStatus getStatus() {
        return getStatusvalue();
    }

    public void isUserActive() {
        DataStatus status = getStatus();
        String USER_STATUS_ERROR = "user status error";
        switch (status) {
            case DELETED -> throw new StatusException(USER_STATUS_ERROR, ErrorCode.STATUS_DELETED);
            case PENDING -> throw new StatusException(USER_STATUS_ERROR, ErrorCode.STATUS_PENDING);
            case BANNED -> throw new StatusException(USER_STATUS_ERROR, ErrorCode.STATUS_BANNED);
        }
    }

    public void isPasswordMatch(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new BaseException((ErrorCode.LOGIN_DENIED));
        }
        ;
    }

}
