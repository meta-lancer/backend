package com.metalancer.backend.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metalancer.backend.common.constants.DataStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
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

    protected void pending() {
        this.status = DataStatus.PENDING;
    }
}