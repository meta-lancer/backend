package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.CreatorList;
import com.metalancer.backend.admin.domain.CreatorPendingList;
import com.metalancer.backend.admin.domain.MemberDetail;
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
import com.metalancer.backend.users.entity.CareerEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.PortfolioEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.ApproveLinkRepository;
import com.metalancer.backend.users.repository.CareerRepository;
import com.metalancer.backend.users.repository.PortfolioImagesRepository;
import com.metalancer.backend.users.repository.PortfolioRepository;
import com.metalancer.backend.users.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AdminMemberServiceImpl implements AdminMemberService {

    private final UserRepository userRepository;
    private final ApproveLinkRepository approveLinkRepository;
    private final CreatorRepository creatorRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioImagesRepository portfolioImagesRepository;
    private final CareerRepository careerRepository;

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

    @Override
    public String deleteMember(Long memberId) {
        User user = userRepository.findById(memberId).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
//        userRepository.delete(user);
        Optional<ApproveLink> approveLink = approveLinkRepository.findByEmail(user.getEmail());
        approveLink.ifPresent(ApproveLink::deleteLink);
        user.deleteUser();
        Optional<CreatorEntity> creator = creatorRepository.findOptionalByUserAndStatus(user,
            DataStatus.ACTIVE);
        creator.ifPresent(CreatorEntity::deleteCreator);
        return "삭제했습니다.";
    }

    @Override
    public String improveToCreator(Long memberId) {
        User user = userRepository.findById(memberId).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
        CreatorEntity createdCreator = CreatorEntity.builder().user(user)
            .email(user.getEmail()).build();
        user.changeToCreator();
        creatorRepository.save(createdCreator);
        return "판매자 전환했습니다.";
    }

    @Override
    public MemberDetail getAdminMemberDetail(Long memberId) {
        User user = userRepository.findById(memberId).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
        Optional<CreatorEntity> creator = creatorRepository.findOptionalByUserAndStatus(user,
            DataStatus.ACTIVE);
        boolean isCreator = creator.isPresent();
        return MemberDetail.builder().user(user).isCreator(isCreator).build();
    }

    @Override
    public String rejectCreator(Long memberId) {
        User user = userRepository.findById(memberId).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
        CreatorEntity foundPendingCreator = creatorRepository.findByUserAndStatus(user,
            DataStatus.PENDING);

        // 만약 등록한 포트폴리오가 있다면
        Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository.findOptionalByCreator(
            foundPendingCreator);
        if (portfolioEntityOptional.isPresent()) {
            PortfolioEntity portfolioEntity = portfolioEntityOptional.get();
            portfolioImagesRepository.deleteAllByPortfolioEntity(portfolioEntity);
            portfolioRepository.delete(portfolioEntity);
        }
        creatorRepository.delete(foundPendingCreator);
        return null;
    }

    @Override
    public Page<CreatorPendingList> getAdminCreatorPendingList(Pageable pageable) {
// creator가 있으나 pending인 녀석만...!
        Page<CreatorEntity> creatorEntities = creatorRepository.findAllByStatusAndActiveUserAndPageable(
            DataStatus.PENDING, pageable);
        List<CreatorPendingList> response = new ArrayList<>();
        for (CreatorEntity creatorEntity : creatorEntities.getContent()) {
            CreatorPendingList creatorPendingList = creatorEntity.toAdminCreatorPendingList();
            Optional<PortfolioEntity> portfolioEntityOptional = portfolioRepository.findOptionalByCreator(
                creatorEntity);
            Optional<CareerEntity> careerEntityOptional = careerRepository.findOptionalByCreator(
                creatorEntity);
            portfolioEntityOptional.ifPresent(creatorPendingList::setPortfolio);
            careerEntityOptional.ifPresent(creatorPendingList::setCareer);
            response.add(creatorPendingList);
        }

        return new PageImpl<>(response, pageable, creatorEntities.getTotalElements());
    }

    private String adminApproveMember(List<User> userList) {
        int count = 0;
        for (User user : userList) {
            user.setActive();
            Optional<ApproveLink> approveLink = approveLinkRepository.findByEmailAndApprovedAtIsNull(
                user.getEmail());
            approveLink.ifPresent(ApproveLink::approve);
            count++;
        }
        return count + "명 승인 완료했습니다.";
    }


}