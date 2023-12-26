package com.metalancer.backend.users.repository;

import com.metalancer.backend.admin.domain.InquiryList;
import com.metalancer.backend.users.entity.InquiryEntity;
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
    public Page<InquiryList> findAdminAll(Pageable pageable) {
        return inquiryJpaRepository.findAllBy(pageable).map(InquiryEntity::toInquiryList);
    }

    @Override
    public Integer countNewCnt() {
        return inquiryJpaRepository.countAllByReplyIsFalse();
    }
}
