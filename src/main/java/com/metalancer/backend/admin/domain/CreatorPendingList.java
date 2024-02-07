package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.users.domain.Career;
import com.metalancer.backend.users.domain.Portfolio;
import com.metalancer.backend.users.entity.CareerEntity;
import com.metalancer.backend.users.entity.PortfolioEntity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatorPendingList {

    private Long memberId;
    private Long creatorId;
    private String email;
    private String name;
    private String username;
    private String mobile;
    private String job;
    private Role role;
    private String loginType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DataStatus status;
    private Portfolio portfolio;
    private Career career;

    @Builder
    public CreatorPendingList(Long memberId, Long creatorId, String email, String name,
        String username,
        String mobile,
        String job, Role role, LoginType loginType, LocalDateTime createdAt,
        LocalDateTime updatedAt,
        DataStatus status) {
        this.memberId = memberId;
        this.creatorId = creatorId;
        this.email = email;
        this.name = name;
        this.username = username;
        this.mobile = mobile;
        this.job = job;
        this.role = role;
        this.loginType = loginType.getProvider();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public void setPortfolio(PortfolioEntity portfolioEntity) {
        this.portfolio = portfolioEntity.toDomain();
    }

    public void setCareer(CareerEntity careerEntity) {
        this.career = careerEntity.toDomain();
    }
}