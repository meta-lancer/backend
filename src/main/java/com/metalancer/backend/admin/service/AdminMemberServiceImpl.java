package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.dto.CreatorList;
import com.metalancer.backend.admin.dto.MemberList;
import com.metalancer.backend.admin.dto.RegisterList;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.users.entity.ApproveLink;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.ApproveLinkRepository;
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
    private final ApproveLinkRepository approveLinkRepository;

    @Override
    public List<MemberList> getAdminMemberList() {
        return userRepository.findAll().stream().map(User::toAdminMemberList)
            .filter(user -> !user.getStatus().equals(DataStatus.PENDING))
            .collect(Collectors.toList());
    }

    @Override
    public List<RegisterList> getAdminRegisterList() {
        List<RegisterList> response = userRepository.findAll().stream()
            .map(User::toAdminRegisterList)
            .filter(user -> user.getStatus().equals(DataStatus.PENDING))
            .toList();
        for (RegisterList registerMember : response) {
            ApproveLink foundApproveLink = approveLinkRepository.findByEmail(
                    registerMember.getEmail())
                .orElseThrow(
                    () -> new BaseException(ErrorCode.NOT_FOUND)
                );
            String approveLink = foundApproveLink.getApproveLink();
            String receivedEmail = foundApproveLink.getEmail();
            boolean approveStatus = foundApproveLink.isApproved();
            registerMember.setApproveInfo(approveLink, receivedEmail, approveStatus);
        }
        return response;
    }

    @Override
    public List<CreatorList> getAdminCreatorList() {
        return userRepository.findAll().stream().map(User::toAdminCreatorList)
            .filter(user -> user.getRole().equals(Role.ROLE_SELLER))
            .collect(Collectors.toList());
    }
}