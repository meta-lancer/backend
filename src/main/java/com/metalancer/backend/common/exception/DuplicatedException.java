package com.metalancer.backend.common.exception;

import com.metalancer.backend.common.constants.ErrorCode;

public class DuplicatedException extends BaseException {

    public DuplicatedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DuplicatedException(String errorMsg, ErrorCode errorCode) {
        super(errorMsg, errorCode);
    }

}