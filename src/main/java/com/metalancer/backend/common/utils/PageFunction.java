package com.metalancer.backend.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageFunction {

    public static Pageable convertToOneBasedPageable(Pageable pageable) {
        int adjustedPage = pageable.getPageNumber() - 1;
        return PageRequest.of(adjustedPage, pageable.getPageSize(), pageable.getSort());
    }

    public static Pageable convertToOneBasedPageableDescending(Pageable pageable) {
        int adjustedPage = pageable.getPageNumber() - 1;
        return PageRequest.of(adjustedPage, pageable.getPageSize(),
            Sort.by("createdAt").descending());
    }
}