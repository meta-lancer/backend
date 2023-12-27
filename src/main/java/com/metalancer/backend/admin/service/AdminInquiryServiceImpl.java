package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.InquiryList;
import com.metalancer.backend.admin.dto.AdminMemberDTO.CreateUpdateReply;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.InvalidRoleException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.users.entity.InquiryEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.InquiryRepository;
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
public class AdminInquiryServiceImpl implements AdminInquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Override
    public Page<InquiryList> getAdminInquiryList(Pageable pageable) {
        return inquiryRepository.findAdminAll(pageable);
    }

    @Override
    public Integer getAdminInquiryNewCount() {
        return inquiryRepository.countNewCnt();
    }

    @Override
    public Boolean replyInquiry(PrincipalDetails user, Long inquiryId, CreateUpdateReply dto) {
        User foundUser = user.getUser();
        foundUser = userRepository.findById(foundUser.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        if (!foundUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new InvalidRoleException(ErrorCode.ROLE_INVALID);
        }
        Long adminId = foundUser.getId();
        String adminName = foundUser.getName();
        String replyContent = dto.getReplyContent();
        InquiryEntity inquiryEntity = inquiryRepository.findEntityById(inquiryId);
        inquiryEntity.reply(adminId, adminName, replyContent);

        return inquiryEntity.getReplyContent().equals(replyContent);
    }
}