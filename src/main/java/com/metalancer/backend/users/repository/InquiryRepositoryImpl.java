package com.metalancer.backend.users.repository;

import com.metalancer.backend.admin.domain.InquiryList;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.users.domain.MyInquiryList;
import com.metalancer.backend.users.entity.InquiryEntity;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InquiryRepositoryImpl implements InquiryRepository {


    private final InquiryJpaRepository inquiryJpaRepository;

    @Override
    public void save(InquiryEntity inquiryEntity) {
        inquiryJpaRepository.save(inquiryEntity);
    }

    @Override
    public Optional<InquiryEntity> findById(Long id) {
        return inquiryJpaRepository.findById(id);
    }

    @Override
    public Optional<InquiryEntity> findByIdAndStatus(Long id, DataStatus status) {
        return inquiryJpaRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Page<InquiryList> findAdminAll(Pageable pageable) {
        return inquiryJpaRepository.findAllBy(pageable).map(InquiryEntity::toInquiryList);
    }

    @Override
    public Integer countNewCnt() {
        return inquiryJpaRepository.countAllByReplyIsFalse();
    }

    @Override
    public InquiryEntity findEntityById(Long inquiryId) {
        return inquiryJpaRepository.findById(inquiryId).orElseThrow(
            () -> new NotFoundException("inquiry", ErrorCode.NOT_FOUND)
        );
    }

    @Override
    public Page<MyInquiryList> findAllByUser(User user, Pageable pageable) {
        return inquiryJpaRepository.findAllByUserAndStatus(user, DataStatus.ACTIVE, pageable)
            .map(InquiryEntity::toMyInquiryList);
    }

    @Override
    public InquiryEntity findEntityByIdAndUser(Long inquiryId, User foundUser) {
        return inquiryJpaRepository.findByIdAndUserAndStatus(inquiryId, foundUser,
            DataStatus.ACTIVE).orElseThrow(
            () -> new NotFoundException("문의", ErrorCode.NOT_FOUND)
        );
    }
}
