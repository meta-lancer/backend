package com.metalancer.backend.admin.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public Boolean checkIfAdmin(PrincipalDetails user) {
        if (user == null) {
            return false;
        }
        return Role.ROLE_ADMIN.equals(user.getUser().getRole());
    }
}