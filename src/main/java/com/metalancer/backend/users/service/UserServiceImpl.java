package com.metalancer.backend.users.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public boolean updateToCreator(PrincipalDetails user) {
        try {
            user.getUser().changeToCreator();

            // 이외 데이터 처리

            return true;
        } catch (BaseException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}