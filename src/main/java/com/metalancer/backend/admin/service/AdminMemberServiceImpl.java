package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.CreatorList;
import com.metalancer.backend.admin.domain.MemberList;
import com.metalancer.backend.admin.domain.RegisterList;
import com.metalancer.backend.admin.dto.AdminMemberDTO.Approve;
import com.metalancer.backend.admin.dto.AdminMemberDTO.UpdateUser;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.creators.repository.CreatorRepository;
import com.metalancer.backend.users.entity.ApproveLink;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.ApproveLinkRepository;
import com.metalancer.backend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AdminMemberServiceImpl implements AdminMemberService {

    private final UserRepository userRepository;
    private final ApproveLinkRepository approveLinkRepository;
    private final CreatorRepository creatorRepository;

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
            Optional<ApproveLink> foundApproveLink = approveLinkRepository.findByEmail(
                    registerMember.getEmail());
            if (foundApproveLink.isPresent()) {
                String approveLink = foundApproveLink.get().getApproveLink();
                String receivedEmail = foundApproveLink.get().getEmail();
                boolean approveStatus = foundApproveLink.get().isApproved();
                registerMember.setApproveInfo(approveLink, receivedEmail, approveStatus);
            }
        }
        return response;
    }

    @Override
    public List<CreatorList> getAdminCreatorList() {
        return creatorRepository.findAll().stream().map(CreatorEntity::toAdminCreatorList)
                .filter(user -> user.getRole().equals(Role.ROLE_SELLER))
                .collect(Collectors.toList());
    }

    @Override
    public String approveUserList(Approve dto) {
        List<User> userList = new ArrayList<>();
        for (Long userId : dto.getUserIdList()) {
            Optional<User> user = userRepository.findById(userId);
            user.ifPresent(userList::add);
        }
        return adminApproveMember(userList);
    }

    @Override
    public MemberList updateMember(UpdateUser dto) {
        User user = userRepository.findById(dto.getMemberId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
        user.update(dto.getName(), dto.getUsername(), dto.getMobile(), dto.getJob(), dto.getRole(),
                dto.getStatus());
        User updatedUser = userRepository.save(user);
        return updatedUser.toAdminMemberList();
    }

    private String adminApproveMember(List<User> userList) {
        int count = 0;
        for (User user : userList) {
            user.setActive();
            Optional<ApproveLink> approveLink = approveLinkRepository.findByEmailAndApprovedAtIsNull(
                    user.getEmail());
            if (approveLink.isPresent()) {
                approveLink.get().approve();
                count++;
            }
        }
        return count + "명 승인 완료했습니다.";
    }
}