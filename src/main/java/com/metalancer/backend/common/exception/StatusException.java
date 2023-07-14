package com.metalancer.backend.common.exception;

import com.metalancer.backend.common.constants.ErrorCode;

public class StatusException extends BaseException {

    public StatusException(ErrorCode errorCode) {
        super(errorCode);
    }

    public StatusException(String errorMsg, ErrorCode errorCode) {
        super(errorMsg, errorCode);
    }

}