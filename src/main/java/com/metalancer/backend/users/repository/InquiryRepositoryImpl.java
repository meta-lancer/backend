package com.metalancer.backend.users.repository;

import com.metalancer.backend.users.entity.InquiryEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
