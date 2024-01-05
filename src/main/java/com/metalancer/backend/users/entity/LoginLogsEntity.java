package com.metalancer.backend.users.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "login_logs")
@ToString
public class LoginLogsEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748321177241993156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "login_log_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private String ipAddress;
    private String status;
    private String message;
    private LocalDateTime loginAt = LocalDateTime.now();

    @Builder
    public LoginLogsEntity(User user, String ipAddress, String status, String message) {
        this.user = user;
        this.ipAddress = ipAddress;
        this.status = status;
        this.message = message;
    }
}
