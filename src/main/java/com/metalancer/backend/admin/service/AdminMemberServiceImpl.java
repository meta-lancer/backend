package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.dto.MemberList;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AdminMemberServiceImpl implements AdminMemberService {

    private final UserRepository userRepository;

    @Override
    public List<MemberList> getAdminMemberList() {
        return userRepository.findAll().stream().map(User::toAdminMemberList)
            .collect(Collectors.toList());
    }
}