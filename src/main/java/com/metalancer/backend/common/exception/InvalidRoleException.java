package com.metalancer.backend.common.exception;

import com.metalancer.backend.common.constants.ErrorCode;

public class InvalidRoleException extends BaseException {

    public InvalidRoleException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidRoleException(String errorMsg, ErrorCode errorCode) {
        super(errorMsg, errorCode);
    }

}