package com.metalancer.backend.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.StatusException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "users")
@ToString
public class User extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748309881533993254L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    private String oauthId;
    private String email;
    private String name;
    private String username;
    private String mobile;
    private String job;
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Builder
    public User(String email, String oauthId, String mobile, String password,
        LoginType loginType, String name, String username, String job) {
        this.email = email;
        this.oauthId = oauthId;
        this.mobile = mobile;
        this.password = password;
        this.loginType = loginType;
        this.name = name;
        this.username = username;
        this.job = job;
    }

    public void setNormalUsername() {
        this.username = loginType.getProvider() + "_"
            + UUID.randomUUID().toString().substring(0, 10);
    }

    public void setPending() {
        pend();
    }

    public void setActive() {
        active();
    }

    public void withdraw() {
        delete();
    }

    public void update() {

    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void changeToCreator() {
        if (this.role.equals(Role.ROLE_USER)) {
            setRole(Role.ROLE_SELLER);
        } else {
            String USER_ROLE_ERROR = "user role error";
            throw new StatusException(USER_ROLE_ERROR, ErrorCode.ROLE_INVALID);
        }
    }

    public void isUserStatusEqualsActive() {
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
