package com.metalancer.backend.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metalancer.backend.admin.domain.MemberList;
import com.metalancer.backend.admin.domain.RegisterList;
import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.DataStatusException;
import com.metalancer.backend.interests.domain.Interests;
import com.metalancer.backend.users.dto.UserResponseDTO.BasicInfo;
import com.metalancer.backend.users.dto.UserResponseDTO.OtherCreatorBasicInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private String nickname = "";
    private LocalDateTime nicknameUpdatedAt;
    private String mobile;
    private String job;
    private String link = "";
    private String introduction = "";
    private String careerIntroduction = "";
    private String profileImg = "https://metaovis-user.s3.ap-northeast-2.amazonaws.com/default_profileImg.png";
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    // 프로필 이미지

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

    public void update(String name, String username, String mobile, String job,
        Role role,
        DataStatus status) {
        this.mobile = mobile;
        this.name = name;
        this.username = username;
        this.job = job;
        this.role = role;
        switch (status) {
            case ACTIVE -> active();
            case DELETED -> delete();
            case PENDING -> pend();
            case BANNED -> prohibit();
        }
    }

    public void setEmailIfNotDuplicated(String email, Optional<User> optionalUser) {
        if (optionalUser.isPresent() && optionalUser.get().getStatus().equals(DataStatus.ACTIVE)) {
            switch (optionalUser.get().getLoginType()) {
                case NORMAL -> throw new BaseException(ErrorCode.EMAIL_SIGNUP_DUPLICATED);
                case KAKAO -> throw new BaseException(ErrorCode.KAKAO_SIGNUP_DUPLICATED);
                case NAVER -> throw new BaseException(ErrorCode.NAVER_SIGNUP_DUPLICATED);
                case GOOGLE -> throw new BaseException(ErrorCode.GOOGLE_SIGNUP_DUPLICATED);
            }
        }
        this.email = email;
    }

    public void setCareerIntroduction(String careerIntroduction) {
        this.careerIntroduction = careerIntroduction;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setLink(String link) {
        this.link = link;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void changeToCreator() {
        if (this.role.equals(Role.ROLE_USER)) {
            setRole(Role.ROLE_SELLER);
        } else {
            String USER_ROLE_ERROR = "user role error";
            throw new DataStatusException(USER_ROLE_ERROR, ErrorCode.ROLE_INVALID);
        }
    }

    public void isUserStatusEqualsActive() {
        DataStatus status = getStatus();
        String USER_STATUS_ERROR = "user status error";
        switch (status) {
            case DELETED ->
                throw new DataStatusException(USER_STATUS_ERROR, ErrorCode.STATUS_DELETED);
            case PENDING ->
                throw new DataStatusException(USER_STATUS_ERROR, ErrorCode.STATUS_PENDING);
            case BANNED ->
                throw new DataStatusException(USER_STATUS_ERROR, ErrorCode.STATUS_BANNED);
        }
    }

    public void isPasswordMatch(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new BaseException((ErrorCode.LOGIN_DENIED));
        }
        ;
    }

    public void isOriginalPasswordMatch(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new BaseException((ErrorCode.PASSWORD_NOT_MATCHED));
        }
        ;
    }

    public void changeNewPassword(PasswordEncoder passwordEncoder, String newPassword) {
        this.password = passwordEncoder.encode(newPassword);
    }

    public MemberList toAdminMemberList() {
        return MemberList.builder()
            .memberId(id)
            .email(email)
            .mobile(mobile)
            .name(name)
            .nickname(nickname)
            .job(job)
            .loginType(loginType)
            .role(role)
            .status(getStatus())
            .createdAt(getCreatedAt())
            .updatedAt(getUpdatedAt())
            .build();
    }

    public RegisterList toAdminRegisterList() {
        return RegisterList.builder()
            .memberId(id)
            .email(email)
            .mobile(mobile)
            .name(name)
            .username(username)
            .loginType(loginType)
            .status(getStatus())
            .createdAt(getCreatedAt())
            .updatedAt(getUpdatedAt())
            .build();
    }

    public void updateBasicInfo(String profileImg, String nickname, String introduction,
        String link, String job) {
        setNicknameUpdatedAt(this.nickname, nickname);
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.introduction = introduction;
        this.link = link;
        this.job = job;
    }

    public BasicInfo toBasicInfo(List<Interests> interests) {
        return BasicInfo.builder().profileImg(profileImg)
            .nickname(nickname)
            .email(email)
            .job(job).link(link)
            .introduction(introduction).interestsList(interests).build();
    }

    public OtherCreatorBasicInfo toOtherCreatorBasicInfo() {
        return OtherCreatorBasicInfo.builder().profileImg(profileImg)
            .nickname(nickname)
            .email(email)
            .link(link)
            .introduction(introduction).build();
    }

    public boolean checkNicknameUpdatedBefore() {
        return nicknameUpdatedAt != null;
    }

    public void setNicknameUpdatedAt(String originNickname, String newNickname) {
        if (!originNickname.equals(newNickname)) {
            this.nicknameUpdatedAt = LocalDateTime.now();
        }
    }

    public void deleteUser() {
        this.email = "(deleted)" + email;
        delete();
    }

    public void setFirstNickName(String randomNickName) {
        this.nickname = randomNickName;
    }
}
