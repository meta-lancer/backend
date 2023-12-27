package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.LoginLogsEntity;
import com.metalancer.backend.users.entity.User;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LoginLogsRepositoryImpl implements LoginLogsRepository {

    private final LoginLogsJpaRepository loginLogsJpaRepository;

    @Override
    public void saveLog(User user, String ipAddress) {
        LoginLogsEntity createdLoginLogsEntity = LoginLogsEntity.builder().user(user)
            .ipAddress(ipAddress)
            .build();
        loginLogsJpaRepository.save(createdLoginLogsEntity);
    }

    @Override
    public Integer getLoginCntDaily(LocalDateTime startDate, LocalDateTime startOfNextDay) {
        return loginLogsJpaRepository.getLoginCntDaily(startDate, startOfNextDay);
    }

    @Override
    public Integer getLoginCntYearly(LocalDateTime startDate, LocalDateTime startOfNextMonth) {
        return loginLogsJpaRepository.getLoginCntYearly(startDate, startOfNextMonth);
    }
}
