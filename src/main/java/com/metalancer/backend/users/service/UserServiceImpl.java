package com.metalancer.backend.users.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.users.domain.PayedAssets;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.PayedAssetsRepository;
import com.metalancer.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PayedAssetsRepository payedAssetsRepository;

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

    @Override
    public Page<PayedAssets> getPayedAssetList(User user, Pageable pageable) {
        return payedAssetsRepository.findAllPayedAssetList(user, pageable);
    }
}