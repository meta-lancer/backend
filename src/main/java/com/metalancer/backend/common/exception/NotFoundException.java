package com.metalancer.backend.common.exception;

import com.metalancer.backend.common.constants.ErrorCode;

public class NotFoundException extends BaseException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(String errorMsg, ErrorCode errorCode) {
        super(errorMsg, errorCode);
    }

}