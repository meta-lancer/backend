package com.metalancer.backend.interests.service;

import com.metalancer.backend.interests.domain.Interests;
import com.metalancer.backend.interests.repository.InterestsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InterestsServiceImpl implements InterestsService {

    private final InterestsRepository interestsRepository;

    @Override
    public List<Interests> getInterests() {
        return interestsRepository.findAll();
    }
}