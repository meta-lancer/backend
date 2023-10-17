package com.metalancer.backend.interests.repository;

import com.metalancer.backend.interests.domain.Interests;
import java.util.List;

public interface InterestsRepository {

    List<Interests> findAll();
}
