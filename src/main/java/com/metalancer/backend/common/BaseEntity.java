package com.metalancer.backend.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metalancer.backend.common.constants.DataStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@DynamicInsert
@NoArgsConstructor
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private DataStatus status = DataStatus.ACTIVE;

    protected void delete() {
        this.status = DataStatus.DELETED;
    }

    protected void restore() {
        this.status = DataStatus.ACTIVE;
    }

    protected void prohibit() {
        this.status = DataStatus.BANNED;
    }

    protected void pend() {
        this.status = DataStatus.PENDING;
    }

    protected DataStatus getStatusvalue() {
        return status;
    }
}