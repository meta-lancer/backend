package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.User;
import java.time.LocalDateTime;

public interface LoginLogsRepository {

    void saveLog(User foundUser, String ipAddress);

    Integer getLoginCntDaily(LocalDateTime date, LocalDateTime startOfNextDay);

    Integer getLoginCntYearly(LocalDateTime date, LocalDateTime startOfNextMonth);
}
