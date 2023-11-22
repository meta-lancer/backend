package com.metalancer.backend.common.exception;

import com.metalancer.backend.common.constants.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

@Slf4j
@Getter
public class DuplicatedUserException extends AuthenticationException {

    private final ErrorCode errorCode;

    public DuplicatedUserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}